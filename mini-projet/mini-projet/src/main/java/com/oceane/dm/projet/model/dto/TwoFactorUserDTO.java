package com.oceane.dm.projet.model.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TwoFactorUserDTO {
    private Long id;

    @NotBlank(message = "Identifier is mandatory")
    private String identifier;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    private String company;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Invalid email format"
    )
    private String email;

    private boolean auth2Fa;
    private String secret;
    private String role;

    @NotBlank
    private String captchaAnswer;

    @NotBlank
    private String captchaSession;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAuth2Fa() {
        return auth2Fa;
    }

    public void setAuth2Fa(boolean auth2Fa) {
        this.auth2Fa = auth2Fa;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public @NotBlank String getCaptchaSession() {
        return captchaSession;
    }

    public void setCaptchaSession(@NotBlank String captchaSession) {
        this.captchaSession = captchaSession;
    }

    public @NotBlank String getCaptchaAnswer() {
        return captchaAnswer;
    }

    public void setCaptchaAnswer(@NotBlank String captchaAnswer) {
        this.captchaAnswer = captchaAnswer;
    }
}

