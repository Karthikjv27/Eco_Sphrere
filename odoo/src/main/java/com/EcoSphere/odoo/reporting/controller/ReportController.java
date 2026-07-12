package com.EcoSphere.odoo.reporting.controller;

import com.EcoSphere.odoo.department.service.DepartmentService;
import com.EcoSphere.odoo.environmental.carbon.service.CarbonTransactionService;
import com.EcoSphere.odoo.environmental.goal.service.EnvironmentalGoalService;
import com.EcoSphere.odoo.governance.policy.service.PolicyService;
import com.EcoSphere.odoo.scoring.dto.ESGScoreDTO;
import com.EcoSphere.odoo.scoring.service.ESGScoreService;
import com.EcoSphere.odoo.social.csr.service.CSRActivityService;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class ReportController {

    private final DepartmentService departmentService;
    private final CarbonTransactionService carbonService;
    private final EnvironmentalGoalService goalService;
    private final CSRActivityService csrService;
    private final PolicyService policyService;
    private final ESGScoreService scoreService;

    public ReportController(DepartmentService departmentService,
                            CarbonTransactionService carbonService,
                            EnvironmentalGoalService goalService,
                            CSRActivityService csrService,
                            PolicyService policyService,
                            ESGScoreService scoreService) {
        this.departmentService = departmentService;
        this.carbonService = carbonService;
        this.goalService = goalService;
        this.csrService = csrService;
        this.policyService = policyService;
        this.scoreService = scoreService;
    }

    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments().size());
        model.addAttribute("carbon", carbonService.getAll().size());
        model.addAttribute("goals", goalService.getAll().size());
        model.addAttribute("csr", csrService.getAll().size());
        model.addAttribute("policies", policyService.getAll().size());
        model.addAttribute("score", scoreService.calculateScore());

        return "reports";
    }

    @GetMapping("/reports/export/pdf")
    public ResponseEntity<byte[]> exportPdf() throws Exception {
        Map<String, String> metrics = createMetrics();
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, output);
            document.open();
            document.add(new Paragraph("EcoSphere ESG Report"));
            document.add(new Paragraph("Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.addCell("Metric");
            table.addCell("Value");
            metrics.forEach((name, value) -> {
                table.addCell(name);
                table.addCell(value);
            });
            document.add(table);
            document.close();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ecosphere-report.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(output.toByteArray());
        }
    }

    @GetMapping("/reports/export/excel")
    public ResponseEntity<byte[]> exportExcel() throws Exception {
        Map<String, String> metrics = createMetrics();
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("ESG Report");
            int rowIndex = 0;
            Row headerRow = sheet.createRow(rowIndex++);
            headerRow.createCell(0).setCellValue("Metric");
            headerRow.createCell(1).setCellValue("Value");

            for (Map.Entry<String, String> entry : metrics.entrySet()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(entry.getKey());
                row.createCell(1).setCellValue(entry.getValue());
            }

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            workbook.write(output);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ecosphere-report.xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(output.toByteArray());
        }
    }

    @GetMapping("/reports/export/csv")
    public ResponseEntity<byte[]> exportCsv() {
        Map<String, String> metrics = createMetrics();
        StringBuilder csv = new StringBuilder();
        csv.append("Metric,Value\n");
        metrics.forEach((name, value) -> csv.append('"').append(name).append("\",\"").append(value).append("\n"));

        byte[] body = csv.toString().getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ecosphere-report.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(body);
    }

    private Map<String, String> createMetrics() {
        Map<String, String> metrics = new LinkedHashMap<>();
        ESGScoreDTO scoreDto = scoreService.calculateScore();
        metrics.put("Departments", String.valueOf(departmentService.getAllDepartments().size()));
        metrics.put("Carbon Transactions", String.valueOf(carbonService.getAll().size()));
        metrics.put("Environmental Goals", String.valueOf(goalService.getAll().size()));
        metrics.put("CSR Activities", String.valueOf(csrService.getAll().size()));
        metrics.put("Policies", String.valueOf(policyService.getAll().size()));
        metrics.put("ESG Score", scoreDto.getOverallScore() + "%");
        return metrics;
    }
}