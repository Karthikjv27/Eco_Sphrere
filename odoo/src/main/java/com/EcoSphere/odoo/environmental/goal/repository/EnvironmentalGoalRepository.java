package com.EcoSphere.odoo.environmental.goal.repository;

import com.EcoSphere.odoo.environmental.goal.entity.EnvironmentalGoal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnvironmentalGoalRepository extends JpaRepository<EnvironmentalGoal,Long> {
}
