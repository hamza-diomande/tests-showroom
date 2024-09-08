package com.oceane.dm.feedback.model;


import com.oceane.dm.models.model.User;
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
    private User.Role role = User.Role.USER_ROLE;






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

    public @NotNull User.Role getRole() {
        return role;
    }

    public void setRole(@NotNull User.Role role) {
        this.role = role;
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
