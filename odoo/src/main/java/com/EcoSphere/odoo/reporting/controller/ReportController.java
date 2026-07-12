package com.EcoSphere.odoo.reporting.controller;

import com.EcoSphere.odoo.department.service.DepartmentService;
import com.EcoSphere.odoo.environmental.carbon.service.CarbonTransactionService;
import com.EcoSphere.odoo.environmental.goal.service.EnvironmentalGoalService;
import com.EcoSphere.odoo.governance.policy.service.PolicyService;
import com.EcoSphere.odoo.scoring.service.ESGScoreService;
import com.EcoSphere.odoo.social.csr.service.CSRActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}