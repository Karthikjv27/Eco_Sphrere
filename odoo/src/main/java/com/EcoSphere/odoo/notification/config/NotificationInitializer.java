package com.EcoSphere.odoo.notification.config;

import com.EcoSphere.odoo.common.enums.NotificationPriority;
import com.EcoSphere.odoo.common.enums.NotificationType;
import com.EcoSphere.odoo.notification.entity.Notification;
import com.EcoSphere.odoo.notification.service.NotificationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class NotificationInitializer implements CommandLineRunner {

    private final NotificationService notificationService;

    public NotificationInitializer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void run(String... args) {
        if (notificationService.getAll().isEmpty()) {
            List<Notification> notifications = List.of(
                    Notification.builder()
                            .title("Policy Review Reminder")
                            .message("Your HR policy will need review in 10 days. Schedule a governance meeting.")
                            .type(NotificationType.REMINDER)
                            .priority(NotificationPriority.MEDIUM)
                            .readStatus(false)
                            .createdAt(LocalDateTime.now().minusHours(3))
                            .build(),
                    Notification.builder()
                            .title("Carbon Emission Alert")
                            .message("Transportation emissions for Q2 exceeded the departmental target.")
                            .type(NotificationType.WARNING)
                            .priority(NotificationPriority.HIGH)
                            .readStatus(false)
                            .createdAt(LocalDateTime.now().minusDays(1))
                            .build(),
                    Notification.builder()
                            .title("CSR Event Scheduled")
                            .message("A new community clean-up event is scheduled for next week.")
                            .type(NotificationType.SUCCESS)
                            .priority(NotificationPriority.LOW)
                            .readStatus(false)
                            .createdAt(LocalDateTime.now().minusDays(2))
                            .build(),
                    Notification.builder()
                            .title("System Health Check")
                            .message("Weekly ESG dashboard health check completed successfully.")
                            .type(NotificationType.SYSTEM)
                            .priority(NotificationPriority.LOW)
                            .readStatus(true)
                            .createdAt(LocalDateTime.now().minusDays(5))
                            .build()
            );

            notifications.forEach(notificationService::save);
        }
    }
}
