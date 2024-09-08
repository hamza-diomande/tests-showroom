package com.oceane.dm.projet.config;


import com.oceane.dm.projet.service.CaptchaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CaptchaServiceConfig {


    /**
     * Modèle rest {@link RestTemplate} pour le service de captcha.
     *
     * @return le {@link RestTemplate}
     */
    @Bean(name = "captchaServiceRestTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }



}
