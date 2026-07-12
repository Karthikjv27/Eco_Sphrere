package com.EcoSphere.odoo.scoring.controller;


import com.EcoSphere.odoo.scoring.dto.ESGScoreDTO;
import com.EcoSphere.odoo.scoring.service.ESGScoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ESGScoreController {

    private final ESGScoreService service;

    public ESGScoreController(ESGScoreService service) {
        this.service = service;
    }

    @GetMapping("/score")
    public ESGScoreDTO score() {
        return service.calculateScore();
    }
}