package com.EcoSphere.odoo.governance.policy.controller;


import com.EcoSphere.odoo.department.entity.Department;
import com.EcoSphere.odoo.department.service.DepartmentService;
import com.EcoSphere.odoo.governance.policy.entity.Policy;
import com.EcoSphere.odoo.governance.policy.service.PolicyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/policy")
public class PolicyController {

    private final PolicyService policyService;
    private final DepartmentService departmentService;

    public PolicyController(PolicyService policyService,
                            DepartmentService departmentService) {
        this.policyService = policyService;
        this.departmentService = departmentService;
    }

    @GetMapping("/page")
    public String page(Model model){

        model.addAttribute("policies",policyService.getAll());
        model.addAttribute("departments",departmentService.getAllDepartments());

        return "policy";
    }

    @PostMapping("/save")
    public String save(@RequestParam String policyName,
                       @RequestParam String description,
                       @RequestParam String status,
                       @RequestParam Long departmentId){

        Department department=departmentService.getDepartmentById(departmentId);

        Policy policy=Policy.builder()
                .policyName(policyName)
                .description(description)
                .status(status)
                .department(department)
                .build();

        policyService.save(policy);

        return "redirect:/policy/page";
    }

}