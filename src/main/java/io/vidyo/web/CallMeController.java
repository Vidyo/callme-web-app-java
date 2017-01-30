package io.vidyo.web;

import io.vidyo.config.VidyoProperties;
import io.vidyo.domain.CallMeConfig;
import io.vidyo.repository.CallMeConfigRepository;
import io.vidyo.service.ConferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;

@Controller
public class CallMeController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ConferenceService conferenceService;

    @Autowired
    CallMeConfigRepository callMeConfigRepository;

    @Inject
    VidyoProperties vidyoProperties;

    // JSONP call
    @RequestMapping(value = "/callme/{appKey}", produces = "application/javascript")
    @ResponseBody String callme(@RequestParam(value = "callback") String callback,
                                @PathVariable(value="appKey") String appKey,
                                HttpServletRequest request, HttpServletResponse response) {

        String incomingReferrer = request.getHeader("referer");
        String incomingHost = "";
        boolean isDemoPage = false;
        if (incomingReferrer != null) {
            try {
                URL url = new URL(incomingReferrer);
                incomingHost = url.getHost().toLowerCase();
                isDemoPage = incomingReferrer.startsWith(vidyoProperties.getCallMe().getAppBaseUrl());
            } catch (MalformedURLException e) {
                log.info("Unparseable referrer: " + incomingReferrer);
            }
        }

        CallMeConfig callMeConfig = callMeConfigRepository.findByAppKey(appKey);
        if (callMeConfig == null) {
            return callback+"( { \"error\" : \"forbidden\"} )";
        }

        if (!isDemoPage) {
            String referrers = callMeConfig.getReferrers().toLowerCase();
            log.debug("Check for referrers [" + referrers + "], actual is [" + incomingReferrer + "]");
            if (!referrers.contains(incomingHost)) {
                log.warn("Disallowed referrer: " + incomingReferrer);
                return callback + "( { \"error\" : \"forbidden\"} )";
            }
        }

        String room = conferenceService.createConferenceRoom(callMeConfig);
        String token = conferenceService.generateToken(callMeConfig.getAppId(), callMeConfig.getDevKey());
        return callback+"( { \"room\": \"" + room + "\", \"token\" : \"" + token + "\"} )";
    }

}
