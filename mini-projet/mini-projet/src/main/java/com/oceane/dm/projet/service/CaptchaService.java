package com.oceane.dm.projet.service;


import com.oceane.dm.projet.exception.CaptchaFailedException;
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
public class CaptchaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaService.class);
    private static final String CAPTCHA_FAILED = "validations.captchaanswer.invalid";

    private final RestTemplate restTemplate;
    private final String phpCaptcheckUrl;

    private String captchekJs;

    /**
     * @param restTemplate    le modèle rest.
     * @param phpCaptcheckUrl l'url du service de captcha
     */
    public CaptchaService(@Qualifier("captchaServiceRestTemplate") RestTemplate restTemplate,
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
        try {
            ChallengeResponse response = CaptcheckClient.verify(phpCaptcheckUrl, session, answer);
            if (response == null || !response.isValid()) {
                throw new CaptchaFailedException("Captcha validation failed");
            }
            return true;
        } catch (IOException e) {
            LOGGER.error("Failed to verify captcha: {}", e.getMessage(), e);
            throw new CaptchaFailedException("Failed to verify captcha due to an IO exception", e);
        }
    }

    public String getCaptcheckJs() {
        return this.captchekJs;
    }
}
