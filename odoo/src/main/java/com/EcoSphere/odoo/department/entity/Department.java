package com.EcoSphere.odoo.department.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;


    private String description;
}
