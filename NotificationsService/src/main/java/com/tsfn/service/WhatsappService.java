package com.tsfn.service;

import com.tsfn.models.NotificatioTwilio;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsappService {

    @Value("${twilio.account_sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth_token}")
    private String AUTH_TOKEN;

    @Value("${twilio.whatsapp_number}")
    private String TWILIO_WHATSAPP_NUMBER;

    public void sendWhatsAppMessage(String to,String sms) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        // Use the recipient's phone number passed in the messageModel
        String recipientPhoneNumber = "whatsapp:" + to;

        // Create a WhatsApp message
        Message message = Message.creator(
                new PhoneNumber(recipientPhoneNumber),
                new PhoneNumber("whatsapp:" + TWILIO_WHATSAPP_NUMBER),
                sms)
                .create();

        System.out.println("WhatsApp message sent successfully. SID: " + message.getSid());
    }
}
