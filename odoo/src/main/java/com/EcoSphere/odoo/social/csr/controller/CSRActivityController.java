package com.EcoSphere.odoo.social.csr.controller;


import com.EcoSphere.odoo.department.entity.Department;
import com.EcoSphere.odoo.department.service.DepartmentService;
import com.EcoSphere.odoo.social.csr.entity.CSRActivity;
import com.EcoSphere.odoo.social.csr.service.CSRActivityService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/csr")
public class CSRActivityController {

    private final CSRActivityService csrService;
    private final DepartmentService departmentService;

    public CSRActivityController(CSRActivityService csrService,
                                 DepartmentService departmentService) {
        this.csrService = csrService;
        this.departmentService = departmentService;
    }

    @GetMapping("/page")
    public String page(Model model) {
        model.addAttribute("activities", csrService.getAll());
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "csr";
    }

    @PostMapping("/save")
    public String save(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam Integer participants,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate activityDate,
            @RequestParam Long departmentId) {

        Department department = departmentService.getDepartmentById(departmentId);

        CSRActivity activity = CSRActivity.builder()
                .title(title)
                .description(description)
                .participants(participants)
                .activityDate(activityDate)
                .department(department)
                .build();

        csrService.save(activity);

        return "redirect:/csr/page";
    }
}