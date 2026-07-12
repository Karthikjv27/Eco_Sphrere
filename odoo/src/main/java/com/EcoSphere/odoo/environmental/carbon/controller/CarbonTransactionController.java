package com.EcoSphere.odoo.environmental.carbon.controller;


import com.EcoSphere.odoo.department.service.DepartmentService;
import com.EcoSphere.odoo.environmental.carbon.entity.CarbonTransaction;
import com.EcoSphere.odoo.environmental.carbon.service.CarbonTransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/carbon")
public class CarbonTransactionController {

    private final CarbonTransactionService carbonTransactionService;
    private final DepartmentService departmentService;

    public CarbonTransactionController(CarbonTransactionService carbonTransactionService,
                                       DepartmentService departmentService) {
        this.carbonTransactionService = carbonTransactionService;
        this.departmentService = departmentService;
    }
    @PostMapping
    @ResponseBody
    public CarbonTransaction create(@RequestBody CarbonTransaction carbonTransaction) {
        return carbonTransactionService.save(carbonTransaction);
    }

    @GetMapping
    @ResponseBody
    public List<CarbonTransaction> getAll() {
        return carbonTransactionService.getAll();
    }

    @GetMapping("/page")
    public String page(Model model) {
        model.addAttribute("transactions", carbonTransactionService.getAll());
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "carbon";
    }
}