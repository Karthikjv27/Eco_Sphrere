package com.EcoSphere.odoo.environmental.carbon.service;

import com.EcoSphere.odoo.department.entity.Department;
import com.EcoSphere.odoo.environmental.carbon.entity.CarbonTransaction;
import com.EcoSphere.odoo.environmental.carbon.repository.CarbonTransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CarbonTransactionService {

    private final CarbonTransactionRepository carbonTransactionRepository;

    public CarbonTransactionService(CarbonTransactionRepository carbonTransactionRepository) {
        this.carbonTransactionRepository = carbonTransactionRepository;
    }

    public CarbonTransaction save(CarbonTransaction carbonTransaction) {
        if (carbonTransaction == null || carbonTransaction.getActivityName() == null
                || carbonTransaction.getTransactionDate() == null
                || carbonTransaction.getDepartment() == null) {
            return carbonTransactionRepository.save(carbonTransaction);
        }

        String normalizedActivity = carbonTransaction.getActivityName().trim();
        Department department = carbonTransaction.getDepartment();
        LocalDate date = carbonTransaction.getTransactionDate();

        return carbonTransactionRepository
                .findByActivityNameIgnoreCaseAndTransactionDateAndDepartment(normalizedActivity, date, department)
                .orElseGet(() -> carbonTransactionRepository.save(CarbonTransaction.builder()
                        .activityName(normalizedActivity)
                        .emissionValue(carbonTransaction.getEmissionValue())
                        .emissionUnit(carbonTransaction.getEmissionUnit())
                        .transactionDate(date)
                        .department(department)
                        .build()));
    }

    public List<CarbonTransaction> getAll() {
        return carbonTransactionRepository.findAll();
    }
}