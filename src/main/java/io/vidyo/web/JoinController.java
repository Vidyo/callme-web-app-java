package io.vidyo.web;

import io.vidyo.domain.CallMeConfig;
import io.vidyo.repository.CallMeConfigRepository;
import io.vidyo.service.ConferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class JoinController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ConferenceService conferenceService;

    @Autowired
    CallMeConfigRepository callMeConfigRepository;

    @RequestMapping("/join/{appKey}/{resourceId}")
    String join(@PathVariable(value = "resourceId") String resourceId,
                @PathVariable(value="appKey") String appKey,
                Model model,
                HttpServletRequest request, HttpServletResponse response) {

        if (!conferenceService.conferenceRoomExists(resourceId)) {
            log.info("Invalid join request for unknown conference [" + resourceId + "] from " + request.getRemoteAddr());
            response.setStatus(HttpStatus.NOT_FOUND.value());
            model.addAttribute("status", 404);
            model.addAttribute("error", "Not found");
            model.addAttribute("message", "The conference was not found or expired.");
            return "error";
        }

        CallMeConfig callMeConfig = callMeConfigRepository.findByAppKey(appKey);

        model.addAttribute("token", conferenceService.generateToken(callMeConfig.getAppId(), callMeConfig.getDevKey()));
        model.addAttribute("resourceId", resourceId);

        log.info("Join request for [" + resourceId + "] from " + request.getRemoteAddr());

        return "VidyoConnector";
    }

    @RequestMapping("/demo/{appKey}")
    String demo(@PathVariable(value="appKey") String appKey, Model model) {
        model.addAttribute("appKey", appKey);
        return "demo";
    }

    @RequestMapping("/demo2/{appKey}")
    String demo2(@PathVariable(value="appKey") String appKey, Model model) {
        model.addAttribute("appKey", appKey);
        return "demo2";
    }
}

