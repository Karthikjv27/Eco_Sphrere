package com.EcoSphere.odoo.social.csr.entity;


import com.EcoSphere.odoo.department.entity.Department;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "csr_activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CSRActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Integer participants;

    private LocalDate activityDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
}