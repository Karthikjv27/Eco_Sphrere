package com.EcoSphere.odoo.config;

import com.EcoSphere.odoo.common.enums.GoalStatus;
import com.EcoSphere.odoo.department.entity.Department;
import com.EcoSphere.odoo.department.repository.DepartmentRepository;
import com.EcoSphere.odoo.environmental.carbon.entity.CarbonTransaction;
import com.EcoSphere.odoo.environmental.carbon.repository.CarbonTransactionRepository;
import com.EcoSphere.odoo.environmental.goal.entity.EnvironmentalGoal;
import com.EcoSphere.odoo.environmental.goal.repository.EnvironmentalGoalRepository;
import com.EcoSphere.odoo.governance.policy.entity.Policy;
import com.EcoSphere.odoo.governance.policy.repository.PolicyRepository;
import com.EcoSphere.odoo.social.csr.entity.CSRActivity;
import com.EcoSphere.odoo.social.csr.repository.CSRActivityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Order(10)
public class SeedDataInitializer implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final CarbonTransactionRepository carbonRepository;
    private final EnvironmentalGoalRepository goalRepository;
    private final CSRActivityRepository csrRepository;
    private final PolicyRepository policyRepository;

    public SeedDataInitializer(DepartmentRepository departmentRepository,
                               CarbonTransactionRepository carbonRepository,
                               EnvironmentalGoalRepository goalRepository,
                               CSRActivityRepository csrRepository,
                               PolicyRepository policyRepository) {
        this.departmentRepository = departmentRepository;
        this.carbonRepository = carbonRepository;
        this.goalRepository = goalRepository;
        this.csrRepository = csrRepository;
        this.policyRepository = policyRepository;
    }

    @Override
    public void run(String... args) {
        csrRepository.deleteAll();
        carbonRepository.deleteAll();
        goalRepository.deleteAll();
        policyRepository.deleteAll();
        departmentRepository.deleteAll();

        Department strategy = departmentRepository.save(Department.builder()
                .name("Sustainability Strategy")
                .description("Leads enterprise ESG strategy, regulatory compliance, and reporting.")
                .build());

        Department operations = departmentRepository.save(Department.builder()
                .name("Operations")
                .description("Optimizes energy, waste, and carbon initiatives across facilities.")
                .build());

        Department corporate = departmentRepository.save(Department.builder()
                .name("Corporate Affairs")
                .description("Manages governance, policy, stakeholder engagement and corporate social responsibility.")
                .build());

        policyRepository.save(Policy.builder()
                .policyName("Climate Risk Management Policy")
                .description("Defines enterprise controls for emissions monitoring, reduction targets, and climate risk resilience.")
                .status("Active")
                .department(strategy)
                .build());

        policyRepository.save(Policy.builder()
                .policyName("Supplier Code of Conduct")
                .description("Establishes ESG criteria for supplier selection, audits, and ethical sourcing.")
                .status("Active")
                .department(corporate)
                .build());

        policyRepository.save(Policy.builder()
                .policyName("ESG Data Governance Policy")
                .description("Sets data quality standards for ESG metrics, disclosures and audit-ready reporting.")
                .status("Draft")
                .department(strategy)
                .build());

        goalRepository.save(EnvironmentalGoal.builder()
                .goalName("Net Zero Operations")
                .description("Reduce scope 1 and 2 emissions by 50% across major facilities.")
                .targetValue(50.0)
                .targetUnit("Percent")
                .targetDate(LocalDate.now().plusYears(5))
                .status(GoalStatus.IN_PROGRESS)
                .department(operations)
                .build());

        goalRepository.save(EnvironmentalGoal.builder()
                .goalName("Water Efficiency Improvement")
                .description("Cut water use intensity by 30% in manufacturing by 2028.")
                .targetValue(30.0)
                .targetUnit("Percent")
                .targetDate(LocalDate.now().plusYears(2))
                .status(GoalStatus.IN_PROGRESS)
                .department(operations)
                .build());

        goalRepository.save(EnvironmentalGoal.builder()
                .goalName("Employee Volunteer Hours")
                .description("Deliver 10,000 community volunteer hours this year.")
                .targetValue(10000.0)
                .targetUnit("Hours")
                .targetDate(LocalDate.now().plusYears(1))
                .status(GoalStatus.IN_PROGRESS)
                .department(corporate)
                .build());

        carbonRepository.save(CarbonTransaction.builder()
                .activityName("Facility Energy Consumption")
                .emissionValue(1240.0)
                .emissionUnit("tCO2e")
                .transactionDate(LocalDate.now().minusMonths(1))
                .department(operations)
                .build());

        carbonRepository.save(CarbonTransaction.builder()
                .activityName("Business Travel")
                .emissionValue(420.0)
                .emissionUnit("tCO2e")
                .transactionDate(LocalDate.now().minusWeeks(3))
                .department(corporate)
                .build());

        carbonRepository.save(CarbonTransaction.builder()
                .activityName("Fleet Efficiency Program")
                .emissionValue(95.0)
                .emissionUnit("tCO2e")
                .transactionDate(LocalDate.now().minusMonths(2))
                .department(strategy)
                .build());

        csrRepository.save(CSRActivity.builder()
                .title("Community Skills Workshop")
                .description("Delivered training sessions for local job seekers in renewable energy careers.")
                .participants(78)
                .activityDate(LocalDate.now().minusDays(15))
                .department(corporate)
                .build());

        csrRepository.save(CSRActivity.builder()
                .title("Supply Chain Sustainability Audits")
                .description("Conducted ESG compliance training for key suppliers.")
                .participants(32)
                .activityDate(LocalDate.now().minusDays(25))
                .department(strategy)
                .build());

        csrRepository.save(CSRActivity.builder()
                .title("Employee Green Week")
                .description("Hosted employee engagement events on waste reduction and wellbeing.")
                .participants(250)
                .activityDate(LocalDate.now().minusDays(7))
                .department(operations)
                .build());
    }
}
