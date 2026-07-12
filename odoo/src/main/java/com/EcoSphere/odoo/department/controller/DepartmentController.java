package com.EcoSphere.odoo.department.controller;

import com.EcoSphere.odoo.department.entity.Department;
import com.EcoSphere.odoo.department.service.DepartmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    @ResponseBody
    public Department createDepartment(@RequestBody Department department) {
        return departmentService.saveDepartment(department);
    }

    @GetMapping
    @ResponseBody
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }
    @GetMapping("/page")
    public String departmentPage(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "departments";
    }

    @PostMapping("/save")
    public String saveDepartment(Department department) {
        departmentService.saveDepartment(department);
        return "redirect:/departments/page";
    }
}