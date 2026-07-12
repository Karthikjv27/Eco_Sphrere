package com.EcoSphere.odoo.notification.controller;

import com.EcoSphere.odoo.notification.service.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/page")
    public String page(Model model) {
        model.addAttribute("notifications", notificationService.getAll());
        model.addAttribute("unreadCount", notificationService.countUnread());
        return "notifications";
    }

    @PostMapping("/read/{id}")
    public String markRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return "redirect:/notifications/page";
    }

    @PostMapping("/read-all")
    public String markAllRead() {
        notificationService.markAllAsRead();
        return "redirect:/notifications/page";
    }
}
