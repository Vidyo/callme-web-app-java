package io.vidyo.service;

import io.vidyo.config.VidyoProperties;
import io.vidyo.domain.CallMeConfig;
import io.vidyo.service.util.GenerateToken;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConferenceService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private ConcurrentHashMap<String, Long> resourceIdsCreated = new ConcurrentHashMap<String,Long>();

    @Inject
    private VidyoProperties vidyoProperties;

    @Inject
    private EmailService emailService;

    @Inject
    private TwilioSmsService smsService;

    public String createConferenceRoom(CallMeConfig callMeConfig) {
        String resourceId =  java.util.UUID.randomUUID().toString().split("\\-")[0];
        resourceIdsCreated.put(resourceId, System.currentTimeMillis() + vidyoProperties.getCallMe().getConferenceTtlMs());
        String conferenceUrl = vidyoProperties.getCallMe().getAppBaseUrl() + "/join/" + callMeConfig.getAppKey() + "/"  + resourceId;
        if (StringUtils.isNotBlank(callMeConfig.getEmailTo())) {
            emailService.sendEmail(callMeConfig.getEmailTo(), callMeConfig.getEmailFrom(), callMeConfig.getEmailSubject(), conferenceUrl);
        }
        if (StringUtils.isNotBlank(callMeConfig.getSmsTo())) {
            smsService.sendSmsMessage(callMeConfig.getSmsTo(), callMeConfig.getEmailSubject() + " : " + conferenceUrl);
        }
        log.info("Conference created at " + conferenceUrl);
        return resourceId;
    }

    public boolean conferenceRoomExists(String resourceId) {
        removeConferenceIfExpired(resourceId);
        return resourceIdsCreated.containsKey(resourceId);
    }

    public String generateToken(String appId, String devKey) {
        String user =  java.util.UUID.randomUUID().toString().replaceAll("\\-","");
        return GenerateToken.generateProvisionToken(devKey, user + "@" + appId, vidyoProperties.getCallMe().getTokenTtlSec(), "");
    }

    @Scheduled(fixedDelay=5*60*1000)
    public void expireConferenceRooms() {
        resourceIdsCreated.forEachKey(1, this::removeConferenceIfExpired);
    }

    public void removeConferenceIfExpired(String resourceId) {
        Long expires = resourceIdsCreated.get(resourceId);
        if (expires == null) {
            return;
        }
        if (System.currentTimeMillis() > expires) {
            log.info("Conference expired " + resourceId);
            resourceIdsCreated.remove(resourceId);
        }
    }

}


