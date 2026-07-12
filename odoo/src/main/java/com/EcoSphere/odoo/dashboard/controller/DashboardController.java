package com.EcoSphere.odoo.dashboard.controller;

import com.EcoSphere.odoo.department.service.DepartmentService;
import com.EcoSphere.odoo.environmental.carbon.service.CarbonTransactionService;
import com.EcoSphere.odoo.environmental.goal.service.EnvironmentalGoalService;
import com.EcoSphere.odoo.governance.policy.service.PolicyService;
import com.EcoSphere.odoo.social.csr.service.CSRActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.EcoSphere.odoo.scoring.service.ESGScoreService;
import lombok.*;


@Controller

public class DashboardController {
    private final ESGScoreService esgScoreService;
    private final DepartmentService departmentService;
    private final CarbonTransactionService carbonTransactionService;
    private final CSRActivityService csrService;
    private final PolicyService policyService;
    private final EnvironmentalGoalService goalService;
    public DashboardController(ESGScoreService esgScoreService, DepartmentService departmentService,
                               CarbonTransactionService carbonTransactionService, CSRActivityService csrService, PolicyService policyService, EnvironmentalGoalService goalService) {
        this.esgScoreService = esgScoreService;
        this.departmentService = departmentService;
        this.carbonTransactionService = carbonTransactionService;
        this.csrService = csrService;
        this.policyService = policyService;
        this.goalService = goalService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {

        model.addAttribute("departmentCount",
                departmentService.getAllDepartments().size());

        model.addAttribute("carbonCount",
                carbonTransactionService.getAll().size());

        model.addAttribute("transactions",
                carbonTransactionService.getAll());

        model.addAttribute("score", esgScoreService.calculateScore());

        model.addAttribute("csrCount", csrService.getAll().size());

        model.addAttribute("policyCount", policyService.getAll().size());

        model.addAttribute("goalCount", goalService.getAll().size());

        return "dashboard";
    }
}