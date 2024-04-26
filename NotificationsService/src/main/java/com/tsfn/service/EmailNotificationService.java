package com.tsfn.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService  {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendNotification(String recipientEmail, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientEmail);
        mailMessage.setText(message);
        javaMailSender.send(mailMessage);
    }
}
