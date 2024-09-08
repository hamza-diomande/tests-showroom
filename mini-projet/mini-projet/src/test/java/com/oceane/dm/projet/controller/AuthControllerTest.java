package com.oceane.dm.projet.controller;

import com.oceane.dm.models.repository.TwoFactorUserRepository;
import com.oceane.dm.projet.model.LoginRequest;
import com.oceane.dm.projet.service.AuthService;
import com.oceane.dm.projet.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private TwoFactorUserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void verifyTwoFactorAuth_success() {
        // Arrange
        Map<String, String> request = new HashMap<>();
        request.put("secret", "testSecret");
        request.put("totpCode", "123456");
        request.put("email", "test@example.com");

        when(authService.verify2FA(any(String.class), any(String.class), any(String.class)))
                .thenReturn("Verified");

        // Act
        ResponseEntity<String> response = authController.verifyTwoFactorAuth(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Verified", response.getBody());
    }

    @Test
    void verifyTwoFactorAuth_missingFields() {
        // Arrange
        Map<String, String> request = new HashMap<>();
        // Missing fields

        // Act
        ResponseEntity<String> response = authController.verifyTwoFactorAuth(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Missing required fields", response.getBody());
    }

    @Test
    void verifyTwoFactorAuth_failure() {
        // Arrange
        Map<String, String> request = new HashMap<>();
        request.put("secret", "testSecret");
        request.put("totpCode", "123456");
        request.put("email", "test@example.com");

        when(authService.verify2FA(any(String.class), any(String.class), any(String.class)))
                .thenThrow(new RuntimeException("Verification failed"));

        // Act
        ResponseEntity<String> response = authController.verifyTwoFactorAuth(request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Verification failed", response.getBody());
    }

    @Test
    void login_success() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("test@example.com","password");
        //loginRequest.setEmail("test@example.com");
        //loginRequest.setPassword("password");

        when(authService.login(any(String.class), any(String.class)))
                .thenReturn("jwtToken");

        // Act
        ResponseEntity<String> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwtToken", response.getBody());
    }

    @Test
    void login_failure() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password");
       // loginRequest.setEmail("test@example.com");
       // loginRequest.setPassword("password");

        when(authService.login(any(String.class), any(String.class)))
                .thenThrow(new RuntimeException("Login failed"));

        // Act
        ResponseEntity<String> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Login failed", response.getBody());
    }
}
