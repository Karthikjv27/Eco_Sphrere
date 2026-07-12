package com.EcoSphere.odoo.environmental.carbon.repository;

import com.EcoSphere.odoo.department.entity.Department;
import com.EcoSphere.odoo.environmental.carbon.entity.CarbonTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CarbonTransactionRepository extends JpaRepository<CarbonTransaction,Long> {
    Optional<CarbonTransaction> findByActivityNameIgnoreCaseAndTransactionDateAndDepartment(String activityName,
                                                                                         LocalDate transactionDate,
                                                                                         Department department);
}
