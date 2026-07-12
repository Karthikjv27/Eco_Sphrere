package com.EcoSphere.odoo.scoring.service;

import com.EcoSphere.odoo.common.enums.GoalStatus;
import com.EcoSphere.odoo.environmental.carbon.entity.CarbonTransaction;
import com.EcoSphere.odoo.environmental.carbon.service.CarbonTransactionService;
import com.EcoSphere.odoo.environmental.goal.entity.EnvironmentalGoal;
import com.EcoSphere.odoo.environmental.goal.service.EnvironmentalGoalService;
import com.EcoSphere.odoo.governance.policy.entity.Policy;
import com.EcoSphere.odoo.governance.policy.service.PolicyService;
import com.EcoSphere.odoo.scoring.dto.ESGScoreDTO;
import com.EcoSphere.odoo.social.csr.entity.CSRActivity;
import com.EcoSphere.odoo.social.csr.service.CSRActivityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ESGScoreService {

    private final CarbonTransactionService carbonService;
    private final EnvironmentalGoalService goalService;
    private final CSRActivityService csrService;
    private final PolicyService policyService;

    public ESGScoreService(CarbonTransactionService carbonService,
                           EnvironmentalGoalService goalService,
                           CSRActivityService csrService,
                           PolicyService policyService) {
        this.carbonService = carbonService;
        this.goalService = goalService;
        this.csrService = csrService;
        this.policyService = policyService;
    }

    public ESGScoreDTO calculateScore() {

        List<CarbonTransaction> carbonTransactions = carbonService.getAll();
        List<EnvironmentalGoal> goals = goalService.getAll();
        List<CSRActivity> csrActivities = csrService.getAll();
        List<Policy> policies = policyService.getAll();

        int environmentalScore = calculateEnvironmentalScore(carbonTransactions, goals);
        int socialScore = calculateSocialScore(csrActivities);
        int governanceScore = calculateGovernanceScore(policies);
        int overallScore = (int) Math.round((environmentalScore + socialScore + governanceScore) / 3.0);

        return ESGScoreDTO.builder()
                .environmentalScore(environmentalScore)
                .socialScore(socialScore)
                .governanceScore(governanceScore)
                .overallScore(overallScore)
                .build();
    }

    private int calculateEnvironmentalScore(List<CarbonTransaction> carbonTransactions,
                                            List<EnvironmentalGoal> goals) {
        double totalEmissions = carbonTransactions.stream()
                .map(CarbonTransaction::getEmissionValue)
                .filter(value -> value != null)
                .mapToDouble(Double::doubleValue)
                .sum();

        long completedGoals = goals.stream()
                .filter(goal -> goal.getStatus() == GoalStatus.COMPLETED)
                .count();

        long inProgressGoals = goals.stream()
                .filter(goal -> goal.getStatus() == GoalStatus.IN_PROGRESS)
                .count();

        int activityPenalty = carbonTransactions.size() * 2;
        int emissionPenalty = (int) Math.min(40, Math.round(totalEmissions / 1000.0));
        int goalBonus = (int) ((completedGoals * 8) + (inProgressGoals * 4));

        return clamp(100 - activityPenalty - emissionPenalty + goalBonus);
    }

    private int calculateSocialScore(List<CSRActivity> csrActivities) {
        if (csrActivities.isEmpty()) {
            return 0;
        }

        int totalParticipants = csrActivities.stream()
                .map(CSRActivity::getParticipants)
                .filter(participants -> participants != null)
                .mapToInt(Integer::intValue)
                .sum();

        int activityScore = Math.min(50, csrActivities.size() * 12);
        int participantScore = Math.min(30, totalParticipants / 10);

        return clamp(20 + activityScore + participantScore);
    }

    private int calculateGovernanceScore(List<Policy> policies) {
        if (policies.isEmpty()) {
            return 0;
        }

        long activePolicies = policies.stream()
                .filter(policy -> "Active".equalsIgnoreCase(policy.getStatus()))
                .count();

        long draftPolicies = policies.stream()
                .filter(policy -> "Draft".equalsIgnoreCase(policy.getStatus()))
                .count();

        int activeScore = (int) Math.min(60, activePolicies * 20);
        int draftScore = (int) Math.min(20, draftPolicies * 10);

        return clamp(20 + activeScore + draftScore);
    }

    private int clamp(int score) {
        return Math.max(0, Math.min(100, score));
    }
}
