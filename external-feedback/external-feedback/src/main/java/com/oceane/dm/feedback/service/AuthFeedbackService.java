package com.oceane.dm.feedback.service;

import com.oceane.dm.feedback.security.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service class used for authentication
 */
@Service
public class AuthFeedbackService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthFeedbackService.class);
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthFeedbackService(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public String login(String email, String password){
        LOGGER.debug("Login request for user {}", email);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtils.generateJwtToken(authentication);
    }
}
