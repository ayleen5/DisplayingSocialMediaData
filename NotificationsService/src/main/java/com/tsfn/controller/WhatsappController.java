package com.tsfn.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tsfn.models.NotificatioTwilio;
import com.tsfn.service.WhatsappService;

@RestController
public class WhatsappController {

    @Autowired
    private WhatsappService whatsappService;

    @PostMapping("/send-whatsapp")
    public String sendWhatsApp(@RequestBody NotificatioTwilio messageModel) {
        whatsappService.sendWhatsAppMessage(messageModel);
        return "WhatsApp message sent successfully!";
    }
}
