package com.EcoSphere.odoo.governance.policy.service;


import com.EcoSphere.odoo.governance.policy.entity.Policy;
import com.EcoSphere.odoo.governance.policy.repository.PolicyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyService {

    private final PolicyRepository repository;

    public PolicyService(PolicyRepository repository) {
        this.repository = repository;
    }

    public Policy save(Policy policy){
        return repository.save(policy);
    }

    public List<Policy> getAll(){
        return repository.findAll();
    }

}