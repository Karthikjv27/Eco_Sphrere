package com.EcoSphere.odoo.governance.policy.repository;

import com.EcoSphere.odoo.governance.policy.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepository extends JpaRepository<Policy,Long> {
}
