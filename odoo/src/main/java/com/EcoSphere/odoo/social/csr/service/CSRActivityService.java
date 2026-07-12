package com.EcoSphere.odoo.social.csr.service;

import com.EcoSphere.odoo.social.csr.entity.CSRActivity;
import com.EcoSphere.odoo.social.csr.repository.CSRActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CSRActivityService {

    private final CSRActivityRepository repository;

    public CSRActivityService(CSRActivityRepository repository) {
        this.repository = repository;
    }

    public CSRActivity save(CSRActivity activity) {
        return repository.save(activity);
    }

    public List<CSRActivity> getAll() {
        return repository.findAll();
    }
}