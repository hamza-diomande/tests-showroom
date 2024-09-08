package com.oceane.dm.projet.model;

import jakarta.validation.constraints.NotBlank;

/**
 * The type Login request.
 */
public class LoginRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
