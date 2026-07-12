package com.EcoSphere.odoo.dashboard.controller;

import com.EcoSphere.odoo.common.enums.GoalStatus;
import com.EcoSphere.odoo.department.service.DepartmentService;
import com.EcoSphere.odoo.environmental.carbon.entity.CarbonTransaction;
import com.EcoSphere.odoo.environmental.carbon.service.CarbonTransactionService;
import com.EcoSphere.odoo.environmental.goal.entity.EnvironmentalGoal;
import com.EcoSphere.odoo.environmental.goal.service.EnvironmentalGoalService;
import com.EcoSphere.odoo.governance.policy.entity.Policy;
import com.EcoSphere.odoo.governance.policy.service.PolicyService;
import com.EcoSphere.odoo.scoring.service.ESGScoreService;
import com.EcoSphere.odoo.social.csr.entity.CSRActivity;
import com.EcoSphere.odoo.social.csr.service.CSRActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    private final ESGScoreService esgScoreService;
    private final DepartmentService departmentService;
    private final CarbonTransactionService carbonTransactionService;
    private final CSRActivityService csrService;
    private final PolicyService policyService;
    private final EnvironmentalGoalService goalService;

    public DashboardController(ESGScoreService esgScoreService, DepartmentService departmentService,
                               CarbonTransactionService carbonTransactionService, CSRActivityService csrService, PolicyService policyService, EnvironmentalGoalService goalService) {
        this.esgScoreService = esgScoreService;
        this.departmentService = departmentService;
        this.carbonTransactionService = carbonTransactionService;
        this.csrService = csrService;
        this.policyService = policyService;
        this.goalService = goalService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {

        List<CarbonTransaction> transactions = carbonTransactionService.getAll();
        List<CSRActivity> csrActivities = csrService.getAll();
        List<Policy> policies = policyService.getAll();
        List<EnvironmentalGoal> goals = goalService.getAll();

        List<CarbonTransaction> recentTransactions = transactions.stream()
                .sorted(Comparator
                        .comparing(CarbonTransaction::getTransactionDate,
                                Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(CarbonTransaction::getId,
                                Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(5)
                .toList();

        model.addAttribute("departmentCount", departmentService.getAllDepartments().size());

        model.addAttribute("carbonCount", transactions.size());

        model.addAttribute("transactions", transactions);

        model.addAttribute("recentTransactions", recentTransactions);

        model.addAttribute("score", esgScoreService.calculateScore());

        model.addAttribute("csrCount", csrActivities.size());

        model.addAttribute("policyCount", policies.size());

        model.addAttribute("goalCount", goals.size());

        addChartData(model, transactions, goals, csrActivities, policies);

        return "dashboard";
    }

    private void addChartData(Model model, List<CarbonTransaction> transactions,
                              List<EnvironmentalGoal> goals, List<CSRActivity> csrActivities,
                              List<Policy> policies) {
        Map<YearMonth, Double> monthlyEmissions = new LinkedHashMap<>();
        YearMonth startMonth = YearMonth.now().minusMonths(5);

        for (int i = 0; i < 6; i++) {
            monthlyEmissions.put(startMonth.plusMonths(i), 0.0);
        }

        Map<String, Double> departmentEmissions = new LinkedHashMap<>();

        for (CarbonTransaction transaction : transactions) {
            double emissionValue = valueOrZero(transaction.getEmissionValue());

            if (transaction.getTransactionDate() != null) {
                YearMonth transactionMonth = YearMonth.from(transaction.getTransactionDate());

                if (monthlyEmissions.containsKey(transactionMonth)) {
                    monthlyEmissions.merge(transactionMonth, emissionValue, Double::sum);
                }
            }

            String departmentName = transaction.getDepartment() != null
                    ? transaction.getDepartment().getName()
                    : "Unassigned";

            departmentEmissions.merge(departmentName, emissionValue, Double::sum);
        }

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM yy");

        model.addAttribute("carbonTrendLabels", monthlyEmissions.keySet().stream()
                .map(month -> month.format(monthFormatter))
                .toList());
        model.addAttribute("carbonTrendData", monthlyEmissions.values().stream()
                .map(this::round)
                .toList());

        List<Map.Entry<String, Double>> topDepartmentEmissions = departmentEmissions.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(5)
                .toList();

        model.addAttribute("departmentEmissionLabels", topDepartmentEmissions.stream()
                .map(Map.Entry::getKey)
                .toList());
        model.addAttribute("departmentEmissionData", topDepartmentEmissions.stream()
                .map(entry -> round(entry.getValue()))
                .toList());

        Map<GoalStatus, Long> goalStatusCounts = new EnumMap<>(GoalStatus.class);
        for (GoalStatus status : GoalStatus.values()) {
            goalStatusCounts.put(status, 0L);
        }

        for (EnvironmentalGoal goal : goals) {
            if (goal.getStatus() != null) {
                goalStatusCounts.merge(goal.getStatus(), 1L, Long::sum);
            }
        }

        model.addAttribute("goalStatusLabels", List.of("Pending", "In Progress", "Completed"));
        model.addAttribute("goalStatusData", List.of(
                goalStatusCounts.get(GoalStatus.PENDING),
                goalStatusCounts.get(GoalStatus.IN_PROGRESS),
                goalStatusCounts.get(GoalStatus.COMPLETED)
        ));

        model.addAttribute("csrPolicyLabels", List.of("CSR Projects", "ESG Policies"));
        model.addAttribute("csrPolicyData", List.of(csrActivities.size(), policies.size()));
    }

    private double valueOrZero(Double value) {
        return value != null ? value : 0.0;
    }

    private double round(Double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
