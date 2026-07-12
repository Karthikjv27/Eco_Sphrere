package com.EcoSphere.odoo.notification.service;

import com.EcoSphere.odoo.notification.entity.Notification;
import com.EcoSphere.odoo.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getAll() {
        return notificationRepository.findAll();
    }

    public List<Notification> getUnread() {
        return notificationRepository.findByReadStatusFalseOrderByCreatedAtDesc();
    }

    public long countUnread() {
        return notificationRepository.findByReadStatusFalseOrderByCreatedAtDesc().size();
    }

    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    public void markAsRead(Long id) {
        notificationRepository.findById(id).ifPresent(notification -> {
            notification.setReadStatus(true);
            notificationRepository.save(notification);
        });
    }

    public void markAllAsRead() {
        getUnread().forEach(notification -> {
            notification.setReadStatus(true);
            notificationRepository.save(notification);
        });
    }
}
