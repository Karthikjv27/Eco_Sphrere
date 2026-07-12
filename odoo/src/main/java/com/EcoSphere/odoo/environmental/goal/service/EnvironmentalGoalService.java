package com.EcoSphere.odoo.environmental.goal.service;

import com.EcoSphere.odoo.department.entity.Department;
import com.EcoSphere.odoo.environmental.goal.entity.EnvironmentalGoal;
import com.EcoSphere.odoo.environmental.goal.repository.EnvironmentalGoalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnvironmentalGoalService {

    private final EnvironmentalGoalRepository repository;

    public EnvironmentalGoalService(EnvironmentalGoalRepository repository) {
        this.repository = repository;
    }

    public EnvironmentalGoal save(EnvironmentalGoal goal) {
        if (goal == null || goal.getGoalName() == null || goal.getDepartment() == null) {
            return repository.save(goal);
        }

        String normalizedGoalName = goal.getGoalName().trim();
        Department department = goal.getDepartment();

        return repository.findByGoalNameIgnoreCaseAndDepartment(normalizedGoalName, department)
                .orElseGet(() -> repository.save(EnvironmentalGoal.builder()
                        .goalName(normalizedGoalName)
                        .description(goal.getDescription())
                        .targetValue(goal.getTargetValue())
                        .targetUnit(goal.getTargetUnit())
                        .targetDate(goal.getTargetDate())
                        .status(goal.getStatus())
                        .department(department)
                        .build()));
    }

    public List<EnvironmentalGoal> getAll() {
        return repository.findAll();
    }
}