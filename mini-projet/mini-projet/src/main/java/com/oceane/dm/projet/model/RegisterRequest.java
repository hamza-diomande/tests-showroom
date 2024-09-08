package com.oceane.dm.projet.model;


import com.oceane.dm.models.model.TwoFactorUser;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class RegisterRequest {
    @NotBlank
    private String identifier;

    @NotBlank
    private String password;

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String company;
    @NotBlank
    private String email;
    @NotNull
    private TwoFactorUser.Role role = TwoFactorUser.Role.USER_ROLE;

    @NotBlank
    private boolean auth2Fa = false;



    @NotBlank
    private String captchaAnswer;

    @NotBlank
    private String captchaSession;

    // Constructeur par défaut
    public RegisterRequest() {}


    public @NotBlank String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(@NotBlank String identifier) {
        this.identifier = identifier;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }

    public @NotBlank String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank String lastName) {
        this.lastName = lastName;
    }

    public @NotBlank String getCompany() {
        return company;
    }

    public void setCompany(@NotBlank String company) {
        this.company = company;
    }

    public @NotBlank String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank String email) {
        this.email = email;
    }

    public @NotNull TwoFactorUser.Role getRole() {
        return role;
    }

    public void setRole(@NotNull TwoFactorUser.Role role) {
        this.role = role;
    }


    public @NotBlank boolean isAuth2Fa() {
        return auth2Fa;
    }

    public void setAuth2Fa(@NotBlank boolean auth2Fa) {
        this.auth2Fa = auth2Fa;
    }

    public String getCaptchaAnswer() {
        return captchaAnswer;
    }

    public void setCaptchaAnswer(String captchaAnswer) {
        this.captchaAnswer = captchaAnswer;
    }

    public String getCaptchaSession() {
        return captchaSession;
    }

    public void setCaptchaSession(String captchaSession) {
        this.captchaSession = captchaSession;
    }
}
