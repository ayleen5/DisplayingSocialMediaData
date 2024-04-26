package com.tsfn.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tsfn.models.NotificatioTwilio;
import com.tsfn.service.EmailNotificationService;

@RestController
public class NotificationController {

    @Autowired
    private EmailNotificationService emailNotificationService;

    @PostMapping("/sendNotification")
    public String sendNotification(@RequestBody NotificatioTwilio request) {
        String recipientEmail = request.getEmail();
        String subject = request.getSubject();
        String message = request.getNotification_text();

        emailNotificationService.sendNotification(recipientEmail, message);
        return "Notification sent successfully";
    }
}
