package com.EcoSphere.odoo.department.service;

import com.EcoSphere.odoo.department.entity.Department;
import com.EcoSphere.odoo.department.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department saveDepartment(Department department) {
        String normalizedName = department.getName() == null ? null : department.getName().trim();
        if (normalizedName == null || normalizedName.isEmpty()) {
            throw new IllegalArgumentException("Department name is required");
        }

        return departmentRepository.findByNameIgnoreCase(normalizedName)
                .orElseGet(() -> departmentRepository.save(Department.builder()
                        .name(normalizedName)
                        .description(department.getDescription())
                        .build()));
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }
}
