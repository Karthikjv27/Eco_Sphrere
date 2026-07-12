package com.EcoSphere.odoo.environmental.carbon.repository;

import com.EcoSphere.odoo.environmental.carbon.entity.CarbonTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarbonTransactionRepository extends JpaRepository<CarbonTransaction,Long> {
}
