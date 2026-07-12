package com.EcoSphere.odoo.environmental.goal.repository;

import com.EcoSphere.odoo.department.entity.Department;
import com.EcoSphere.odoo.environmental.goal.entity.EnvironmentalGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnvironmentalGoalRepository extends JpaRepository<EnvironmentalGoal,Long> {
    Optional<EnvironmentalGoal> findByGoalNameIgnoreCaseAndDepartment(String goalName, Department department);
}
