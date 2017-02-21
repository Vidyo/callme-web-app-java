package io.vidyo.service;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class EmailService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private MailSender mailSender;

    @Async
    public void sendEmail(String to, String from, String subject, String body) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setFrom(from);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        try {
            mailSender.send(mailMessage);
        } catch (MailException me) {
            log.error("Error sending mail to: " + to);
            log.info(ExceptionUtils.getStackTrace(me));
        }
    }
}
