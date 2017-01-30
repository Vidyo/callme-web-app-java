package io.vidyo.service;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import io.vidyo.config.VidyoProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class TwilioSmsService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    VidyoProperties vidyoProperties;


    @Async
    public void sendSmsMessage(String to, String body) {
        Twilio.init(
            vidyoProperties.getTwilio().getAccountSid(),
            vidyoProperties.getTwilio().getAuthToken());

        Message smsMessage = Message.creator(
            new PhoneNumber("+" + to),
            new PhoneNumber(vidyoProperties.getTwilio().getFrom()),
            body).create();
    }
}
