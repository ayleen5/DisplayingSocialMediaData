package com.tsfn.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tsfn.models.NotificatioTwilio;
import com.tsfn.service.SmsService;
import com.twilio.rest.api.v2010.account.call.Notification;

@RestController
public class SmsController {

    @Autowired
    private SmsService smsService;

    @PostMapping("/send-sms")
    public String sendSms(@RequestBody NotificatioTwilio messageModel) {
        smsService.sendSms(messageModel.getRecipint(),messageModel.getNotification_text());
        return "SMS sent successfully!";
    }
}
