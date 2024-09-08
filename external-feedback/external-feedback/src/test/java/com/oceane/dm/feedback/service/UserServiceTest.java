/*package com.oceane.dm.feedback.service;

import com.oceane.dm.feedback.exception.CaptchaFailedException;
import com.oceane.dm.models.model.User;
import com.oceane.dm.models.repository.UserRepository;
import com.oceane.dm.feedback.service.CaptchaService;
import com.oceane.dm.feedback.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CaptchaService captchaService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_IdentifierExists() {
        String identifier = "existingUser";
        String password = "password";
        String firstName = "firstName";
        String lastName = "lastName";
        String email = "newuser@example.com";
        String company = "company";
        User.Role role = User.Role.USER_ROLE;
        String captchaSession = "session";
        String captchaAnswer = "answer";

        when(userRepository.findByIdentifier(identifier)).thenReturn(Optional.of(new User()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.createUser(identifier, password, firstName, lastName, email, company, role, captchaSession, captchaAnswer));

        assertEquals("user.validation.identifier.exists", exception.getMessage());
        verify(userRepository).findByIdentifier(identifier);
        verify(userRepository, never()).findByEmail(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUser_EmailExists() {
        String identifier = "newuser";
        String password = "password";
        String firstName = "firstName";
        String lastName = "lastName";
        String email = "existinguser@example.com";
        String company = "company";
        User.Role role = User.Role.USER_ROLE;
        String captchaSession = "session";
        String captchaAnswer = "answer";

        when(userRepository.findByIdentifier(identifier)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.createUser(identifier, password, firstName, lastName, email, company, role, captchaSession, captchaAnswer));

        assertEquals("user.validation.email.exists", exception.getMessage());
        verify(userRepository).findByIdentifier(identifier);
        verify(userRepository).findByEmail(email);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUser_InvalidCaptcha() {
        String identifier = "newuser";
        String password = "password";
        String firstName = "firstName";
        String lastName = "lastName";
        String email = "newuser@example.com";
        String company = "company";
        User.Role role = User.Role.USER_ROLE;
        String captchaSession = "session";
        String captchaAnswer = "answer";

        doThrow(new CaptchaFailedException("validations.captchaanswer.invalid")).when(captchaService).checkCaptcha(captchaSession, captchaAnswer);

        CaptchaFailedException exception = assertThrows(CaptchaFailedException.class, () ->
                userService.createUser(identifier, password, firstName, lastName, email, company, role, captchaSession, captchaAnswer));

        assertEquals("validations.captchaanswer.invalid", exception.getMessage());
        verify(captchaService).checkCaptcha(captchaSession, captchaAnswer);
        verify(userRepository, never()).findByIdentifier(anyString());
        verify(userRepository, never()).findByEmail(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateAdminUser_Success() {
        String identifier = "admin";
        String password = "admin";
        String firstName = "admin";
        String lastName = "admin";
        String email = "admin@admin.com";
        String company = "";
        User.Role role = User.Role.ADMIN_ROLE;

        when(userRepository.findByIdentifier(identifier)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User user = userService.createAdminUser(identifier, password, firstName, lastName, email, company, role);

        assertNotNull(user);
        assertEquals(identifier, user.getIdentifier());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(email, user.getEmail());
        assertEquals(company, user.getCompany());
        assertEquals(role, user.getRole());

        verify(userRepository).findByIdentifier(identifier);
        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(userRepository).save(user);
    }

    @Test
    void testCreateUser_Success() {
        String identifier = "user";
        String password = "password";
        String firstName = "firstName";
        String lastName = "lastName";
        String email = "user@example.com";
        String company = "company";
        User.Role role = User.Role.USER_ROLE;
        String captchaSession = "session";
        String captchaAnswer = "answer";

        when(userRepository.findByIdentifier(identifier)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        doNothing().when(captchaService).checkCaptcha(captchaSession, captchaAnswer);

        User user = userService.createUser(identifier, password, firstName, lastName, email, company, role, captchaSession, captchaAnswer);

        assertNotNull(user);
        assertEquals(identifier, user.getIdentifier());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(email, user.getEmail());
        assertEquals(company, user.getCompany());
        assertEquals(role, user.getRole());

        verify(captchaService).checkCaptcha(captchaSession, captchaAnswer);
        verify(userRepository).findByIdentifier(identifier);
        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(userRepository).save(user);
    }

    @Test
    void testCreateUser_CaptchaFailed() {
        String identifier = "user";
        String password = "password";
        String firstName = "firstName";
        String lastName = "lastName";
        String email = "user@example.com";
        String company = "company";
        User.Role role = User.Role.USER_ROLE;
        String captchaSession = "session";
        String captchaAnswer = "answer";

        doThrow(new CaptchaFailedException("validations.captchaanswer.invalid")).when(captchaService).checkCaptcha(captchaSession, captchaAnswer);

        CaptchaFailedException exception = assertThrows(CaptchaFailedException.class, () -> userService.createUser(identifier, password, firstName, lastName, email, company, role, captchaSession, captchaAnswer));

        assertEquals("validations.captchaanswer.invalid", exception.getMessage());
        verify(captchaService).checkCaptcha(captchaSession, captchaAnswer);
        verify(userRepository, never()).findByIdentifier(anyString());
        verify(userRepository, never()).findByEmail(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLoadUserByUsername_Success() {
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        String email = "user@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(email));

        assertEquals("Username not found", exception.getMessage());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void testGetCurrentUser() {
        User user = new User();
        user.setEmail("user@example.com");

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContextHolder.setContext(securityContext);

        User currentUser = userService.getCurrentUser();

        assertNotNull(currentUser);
        assertEquals(user, currentUser);
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
    }

}
*/