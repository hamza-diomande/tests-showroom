package com.oceane.dm.feedback.service;

import com.oceane.dm.feedback.exception.CaptchaFailedException;
import fr.anfr.captcheck_java_client.CaptcheckClient;
import fr.anfr.captcheck_java_client.ChallengeResponse;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * Classe de service pour la validation des captcha
 */
@Service
public class CaptchaFeedbackService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaFeedbackService.class);
    private static final String CAPTCHA_FAILED = "validations.captchaanswer.invalid";

    private final RestTemplate restTemplate;
    private final String phpCaptcheckUrl;

    private String captchekJs;

    /**
     * @param restTemplate    le modèle rest.
     * @param phpCaptcheckUrl l'url du service de captcha
     */
    public CaptchaFeedbackService(@Qualifier("captchaServiceRestTemplate") RestTemplate restTemplate,
                                  @Value("${app.phpcaptcheck.url}") String phpCaptcheckUrl) {
        this.restTemplate = restTemplate;
        this.phpCaptcheckUrl = phpCaptcheckUrl;
    }

    @PostConstruct
    public void initCaptcheckJs() {
        this.captchekJs = restTemplate.getForObject(phpCaptcheckUrl + "/captcheck.js", String.class);
    }

    /**
     * Vérification de la validité d'un captcha.
     *
     * @param session la session du captcha
     * @param answer  la réponse du captcha
     * @return vrai si le captcha est valide
     */
    public boolean checkCaptcha(String session, String answer)  {
        boolean result = false;
        try {
            ChallengeResponse response = CaptcheckClient.verify(phpCaptcheckUrl, session, answer);
            result = response != null && response.isValid();
        } catch (IOException e) {
            LOGGER.error("Failed to verify captcha {}", e.getMessage());
        }

        if (!result) {
            throw new CaptchaFailedException(CAPTCHA_FAILED);
        }
        return result;
    }

    public String getCaptcheckJs() {
        return this.captchekJs;
    }
}
