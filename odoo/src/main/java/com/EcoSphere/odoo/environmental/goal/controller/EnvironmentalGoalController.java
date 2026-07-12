package com.EcoSphere.odoo.environmental.goal.controller;
import com.EcoSphere.odoo.common.enums.GoalStatus;
import com.EcoSphere.odoo.department.entity.Department;
import com.EcoSphere.odoo.department.service.DepartmentService;
import com.EcoSphere.odoo.environmental.goal.entity.EnvironmentalGoal;
import com.EcoSphere.odoo.environmental.goal.service.EnvironmentalGoalService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/goals")
public class EnvironmentalGoalController {

    private final EnvironmentalGoalService goalService;
    private final DepartmentService departmentService;

    public EnvironmentalGoalController(EnvironmentalGoalService goalService,
                                       DepartmentService departmentService) {
        this.goalService = goalService;
        this.departmentService = departmentService;
    }

    @GetMapping("/page")
    public String page(Model model) {

        model.addAttribute("goals", goalService.getAll());
        model.addAttribute("departments", departmentService.getAllDepartments());

        return "goals";
    }

    @PostMapping("/save")
    public String saveGoal(
            @RequestParam String goalName,
            @RequestParam String description,
            @RequestParam Double targetValue,
            @RequestParam String targetUnit,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate,
            @RequestParam GoalStatus status,
            @RequestParam Long departmentId) {

        Department department = departmentService.getDepartmentById(departmentId);

        EnvironmentalGoal goal = EnvironmentalGoal.builder()
                .goalName(goalName)
                .description(description)
                .targetValue(targetValue)
                .targetUnit(targetUnit)
                .targetDate(targetDate)
                .status(status)
                .department(department)
                .build();

        goalService.save(goal);

        return "redirect:/goals/page";
    }
}