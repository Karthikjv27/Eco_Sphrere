package com.EcoSphere.odoo.environmental.carbon.service;


import com.EcoSphere.odoo.environmental.carbon.entity.CarbonTransaction;
import com.EcoSphere.odoo.environmental.carbon.repository.CarbonTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarbonTransactionService {

    private final CarbonTransactionRepository carbonTransactionRepository;

    public CarbonTransactionService(CarbonTransactionRepository carbonTransactionRepository) {
        this.carbonTransactionRepository = carbonTransactionRepository;
    }

    public CarbonTransaction save(CarbonTransaction carbonTransaction) {
        return carbonTransactionRepository.save(carbonTransaction);
    }

    public List<CarbonTransaction> getAll() {
        return carbonTransactionRepository.findAll();
    }
}