package com.EcoSphere.odoo.department.service;


import com.EcoSphere.odoo.department.entity.Department;
import com.EcoSphere.odoo.department.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    public DepartmentService(DepartmentRepository departmentRepository){
        this.departmentRepository=departmentRepository;
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

}
