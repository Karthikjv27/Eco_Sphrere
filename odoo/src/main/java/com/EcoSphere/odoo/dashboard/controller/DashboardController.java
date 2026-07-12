package com.EcoSphere.odoo.dashboard.controller;

import com.EcoSphere.odoo.department.service.DepartmentService;
import com.EcoSphere.odoo.environmental.carbon.service.CarbonTransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.EcoSphere.odoo.scoring.service.ESGScoreService;
@Controller
public class DashboardController {
    private final ESGScoreService esgScoreService;
    private final DepartmentService departmentService;
    private final CarbonTransactionService carbonTransactionService;

    public DashboardController(ESGScoreService esgScoreService, DepartmentService departmentService,
                               CarbonTransactionService carbonTransactionService) {
        this.esgScoreService = esgScoreService;
        this.departmentService = departmentService;
        this.carbonTransactionService = carbonTransactionService;
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
        return "dashboard";
    }
}