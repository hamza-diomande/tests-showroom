package com.oceane.dm.feedback.controller;

import com.oceane.dm.feedback.model.LoginRequest;
import com.oceane.dm.feedback.model.RegisterRequest;
import com.oceane.dm.models.model.User;
import com.oceane.dm.feedback.service.AuthFeedbackService;
import com.oceane.dm.feedback.service.UserFeedBackService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The type Auth controller.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthFeedbackService authService;
    private final UserFeedBackService userService;

    /**
     * Instantiates a new Auth controller.
     *
     * @param authService the auth service
     */
    public AuthController(AuthFeedbackService authService, UserFeedBackService userService) {
        this.authService = authService;

        this.userService = userService;
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

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody @Valid RegisterRequest registerRequest) {
        String captchaAnswer = registerRequest.getCaptchaAnswer();
        String captchaSession = registerRequest.getCaptchaSession();

        if (registerRequest.getIdentifier() == null || registerRequest.getFirstName() == null || registerRequest.getLastName().isEmpty() ||
               registerRequest.getEmail() == null || registerRequest.getPassword() == null || registerRequest.getCompany() == null) {
            throw new IllegalArgumentException("Toutes les entrées obligatoires doivent être présentes.");
        }
        // Vérifier que le mot de passe contient au moins 8 caractères
        if (registerRequest.getPassword().length() < 8) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caractères.");
        }
        System.out.println("bonjour");
        User registeredUser = userService.createUser(registerRequest.getIdentifier(), registerRequest.getPassword(), registerRequest.getFirstName(), registerRequest.getLastName(), registerRequest.getEmail(), registerRequest.getCompany(), registerRequest.getRole(), captchaSession, captchaAnswer);
        System.out.println(registeredUser.getId());
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

}
