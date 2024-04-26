package com.tsfn.service;
import com.tsfn.models.*;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.call.Notification;
import com.twilio.type.PhoneNumber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.account_sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth_token}")
    private String AUTH_TOKEN;

    @Value("${twilio.trial_number}")
    private String TWILIO_NUMBER;

//    @Autowired
    private NotificatioTwilio smsNot;
    public void sendSms(String to ,String sms) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        
        Message message = Message.creator(new PhoneNumber(to), new PhoneNumber(TWILIO_NUMBER),sms)
                .create();
        System.out.println("SMS sent successfully. SID: " + message.getSid());
    }
}
