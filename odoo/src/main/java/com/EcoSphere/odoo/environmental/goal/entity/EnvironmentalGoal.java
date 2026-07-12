package com.EcoSphere.odoo.environmental.goal.entity;


import com.EcoSphere.odoo.common.enums.GoalStatus;
import com.EcoSphere.odoo.department.entity.Department;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "environmental_goals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvironmentalGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String goalName;

    private String description;

    private Double targetValue;

    private String targetUnit;

    private LocalDate targetDate;

    @Enumerated(EnumType.STRING)
    private GoalStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
}