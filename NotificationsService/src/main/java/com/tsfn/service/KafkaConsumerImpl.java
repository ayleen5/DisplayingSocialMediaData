
package com.tsfn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.tsfn.models.NotificatioTwilio;
import com.tsfn.service.SmsService;
import com.tsfn.service.EmailNotificationService;
import com.tsfn.service.WhatsappService;

@Service
public class KafkaConsumerImpl {

    private final SmsService smsService;
    private final EmailNotificationService emailNotificationService;
    private final WhatsappService whatsappService;

    public KafkaConsumerImpl(SmsService smsService, EmailNotificationService emailNotificationService, WhatsappService whatsappService) {
        this.smsService = smsService;
        this.emailNotificationService = emailNotificationService;
        this.whatsappService = whatsappService;
    }

    @KafkaListener(topics = "NotificationTopic", groupId = "groupId")
    public void listen(String message) {
        System.out.println("Received message: " + message);
        // Parse the message and call the appropriate function based on the type of "via"
        String[] parts = message.split("_");
        if (parts.length != 3) {
            System.out.println("Invalid message format: " + message);
            return;
        }

        String via = parts[0];
        String to = parts[1];
        String notification = parts[2];

        switch (via) {
            case "sms"://i need to do set here 
                smsService.sendSms(to, notification);
                break;
            case "email":
                emailNotificationService.sendNotification(to, notification);
                break;
            case "whatsapp":
                NotificatioTwilio whatsapp = new NotificatioTwilio(to, notification);
                whatsappService.sendWhatsAppMessage(whatsapp);
                break;
            default:
                System.out.println("Unsupported 'via' type: " + via);
        }
    }
}
