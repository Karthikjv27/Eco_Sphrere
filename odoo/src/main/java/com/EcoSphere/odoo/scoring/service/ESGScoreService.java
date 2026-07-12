package com.EcoSphere.odoo.scoring.service;

import com.EcoSphere.odoo.environmental.carbon.service.CarbonTransactionService;
import com.EcoSphere.odoo.environmental.goal.service.EnvironmentalGoalService;
import com.EcoSphere.odoo.scoring.dto.ESGScoreDTO;
import org.springframework.stereotype.Service;

@Service
public class ESGScoreService {

    private final CarbonTransactionService carbonService;
    private final EnvironmentalGoalService goalService;

    public ESGScoreService(CarbonTransactionService carbonService,
                           EnvironmentalGoalService goalService) {
        this.carbonService = carbonService;
        this.goalService = goalService;
    }

    public ESGScoreDTO calculateScore() {

        int carbonCount = carbonService.getAll().size();

        long completedGoals = goalService.getAll()
                .stream()
                .filter(g -> g.getStatus().name().equals("COMPLETED"))
                .count();

        int environmentalScore = 100 - (carbonCount * 2) + ((int) completedGoals * 5);

        environmentalScore = Math.max(0, Math.min(100, environmentalScore));

        return ESGScoreDTO.builder()
                .environmentalScore(environmentalScore)
                .socialScore(0)
                .governanceScore(0)
                .overallScore(environmentalScore)
                .build();
    }
}