package com.oceane.dm.projet.service;

import com.oceane.dm.models.model.TwoFactorUser;
import com.oceane.dm.projet.security.JwtUtils;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.TimeProvider;
import dev.samstevens.totp.time.SystemTimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private SecretGenerator secretGenerator;

    @Mock
    private QrGenerator qrGenerator;

    @Mock
    private CodeVerifier verifier;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateSecretKey() {
        // Arrange
        String expectedSecret = "testSecret";
        when(secretGenerator.generate()).thenReturn(expectedSecret);

        // Act
        String actualSecret = authService.generateSecretKey();

        // Assert
        assertEquals(expectedSecret, actualSecret);
        verify(secretGenerator, times(1)).generate();
    }

    @Test
    public void testGetQRCode() throws Exception {
        // Arrange
        String secret = "testSecret";
        String account = "testAccount";
        String qrCodeData = "data:image/png;base64,...";
        when(qrGenerator.generate(any())).thenReturn(new byte[0]);
        when(qrGenerator.getImageMimeType()).thenReturn("image/png");
        when(qrGenerator.generate(any())).thenReturn(new byte[0]);

        // Act
        String qrCode = authService.getQRCode(secret, account);

        // Assert
        assertNotNull(qrCode);
        assertTrue(qrCode.startsWith("data:image/png;base64,"));
    }

    /*@Test
    public void testVerifyCode() {
        // Arrange
        String secret = "testSecret";
        String code = "123456";
        when(verifier.isValidCode(secret, code)).thenReturn(true);

        // Act
        boolean result = authService.verifyCode(secret, code);

        // Assert
        assertTrue(result);
        verify(verifier, times(1)).isValidCode(secret, code);
    }*/

    @Test
    public void testLoginWith2FA() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        Authentication authentication = mock(Authentication.class);
        TwoFactorUser user = mock(TwoFactorUser.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(user.isAuth2Fa()).thenReturn(true);

        // Act
        String result = authService.login(email, password);

        // Assert
        assertEquals("2FA_REQUIRED", result);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testLoginWithout2FA() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        Authentication authentication = mock(Authentication.class);
        TwoFactorUser user = mock(TwoFactorUser.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(user.isAuth2Fa()).thenReturn(false);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwtToken");

        // Act
        String result = authService.login(email, password);

        // Assert
        assertEquals("jwtToken", result);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, times(1)).generateJwtToken(authentication);
    }

    /*@Test
    public void testVerify2FAValidCode() {
        // Arrange
        String email = "test@example.com";
        String secret = "testSecret";
        String code = "123456";
        TwoFactorUser user = mock(TwoFactorUser.class);
        when(userService.loadUserByUsername(email)).thenReturn(user);
        when(verifier.isValidCode(secret, code)).thenReturn(true);
        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("jwtToken");

        // Act
        String result = authService.verify2FA(email, secret, code);

        // Assert
        assertEquals("jwtToken", result);
    }*/

    @Test
    public void testVerify2FAInvalidCode() {
        // Arrange
        String email = "test@example.com";
        String secret = "testSecret";
        String code = "123456";
        TwoFactorUser user = mock(TwoFactorUser.class);
        when(userService.loadUserByUsername(email)).thenReturn(user);
        when(verifier.isValidCode(secret, code)).thenReturn(false);

        // Act
        String result = authService.verify2FA(email, secret, code);

        // Assert
        assertEquals("Invalid TOTP code", result);
    }

    @Test
    public void testVerify2FAUserNotFound() {
        // Arrange
        String email = "test@example.com";
        String secret = "testSecret";
        String code = "123456";
        when(userService.loadUserByUsername(email)).thenReturn(null);

        // Act
        String result = authService.verify2FA(email, secret, code);

        // Assert
        assertEquals("User not found", result);
    }
}
