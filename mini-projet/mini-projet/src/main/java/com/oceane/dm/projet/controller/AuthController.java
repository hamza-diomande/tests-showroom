package com.oceane.dm.projet.controller;

import com.oceane.dm.models.repository.TwoFactorUserRepository;
import com.oceane.dm.projet.model.LoginRequest;
import com.oceane.dm.projet.service.AuthService;
import com.oceane.dm.projet.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;  // Renommé pour plus de clarté
    @Autowired
    private UserService userService;  // Non utilisé ici mais potentiellement utile
    @Autowired
    private TwoFactorUserRepository userRepository;  // Non utilisé ici mais potentiellement utile

    /**
     * Verify two-factor authentication code.
     *
     * @param request the request containing secret, totpCode, and email
     * @return verification result
     */
    @PostMapping("/verify")
    public ResponseEntity<String> verifyTwoFactorAuth(@RequestBody Map<String, String> request) {
        String secret = request.get("secret");
        String code = request.get("totpCode");
        String email = request.get("email");

        if (secret == null || code == null || email == null) {
            return ResponseEntity.badRequest().body("Missing required fields");
        }

        try {
            String isVerified = authService.verify2FA(email, secret, code);
            return ResponseEntity.ok(isVerified);
        } catch (Exception e) {
            // Log exception
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Verification failed");
        }
    }

    /**
     * Login a user to get a JWT.
     *
     * @param loginRequest the login request
     * @return the JWT
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            String jwt = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            // Log exception
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
        }
    }



}
