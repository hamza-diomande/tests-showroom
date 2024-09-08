package com.oceane.dm.controller;

import com.oceane.dm.model.LoginRequest;
import com.oceane.dm.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * The type Auth controller.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    /**
     * Instantiates a new Auth controller.
     *
     * @param authService the auth service
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Login a user to get a jwt.
     *
     * @param loginRequest the login request
     * @return the jwt
     */
    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginRequest loginRequest) {
        return authService.login(
                loginRequest.getEmail(),
                loginRequest.getPassword());
    }
}
