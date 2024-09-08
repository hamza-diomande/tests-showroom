package com.oceane.dm.feedback.controller;

import com.oceane.dm.feedback.service.CaptchaFeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Classe de controlleur exposant les fonctions liées aux configurations
 */
@RestController
@RequestMapping("/api/application")
public class ApplicationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);

    private final CaptchaFeedbackService captchaService;

    public ApplicationController(CaptchaFeedbackService captchaService) {
        this.captchaService = captchaService;
    }

    @GetMapping(value = "/captcheck.js", produces = "text/javascript")
    public String getCaptcheckScript() {
        LOGGER.info("Call captcheck");
        return captchaService.getCaptcheckJs();
    }


}
