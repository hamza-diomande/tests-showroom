package com.oceane.dm.projet.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class DummyRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String captchaSession;
    @NotBlank
    private String captchaAnswer;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @NotBlank String getCaptchaAnswer() {
        return captchaAnswer;
    }

    public void setCaptchaAnswer(@NotBlank String captchaAnswer) {
        this.captchaAnswer = captchaAnswer;
    }

    public @NotBlank String getCaptchaSession() {
        return captchaSession;
    }

    public void setCaptchaSession(@NotBlank String captchaSession) {
        this.captchaSession = captchaSession;
    }
}
