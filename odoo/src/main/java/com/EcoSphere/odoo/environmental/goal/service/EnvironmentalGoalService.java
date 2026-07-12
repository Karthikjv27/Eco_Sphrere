package com.EcoSphere.odoo.environmental.goal.service;
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
        return repository.save(goal);
    }

    public List<EnvironmentalGoal> getAll() {
        return repository.findAll();
    }
}