package com.oceane.dm.service;

import com.oceane.dm.model.User;
import com.oceane.dm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*@Test
    public void testCreateUser_Success() {
        String identifier = "new user";
        String password = "password";
        String encodedPassword = "encodedPassword";
        String fName = "John";
        String lName = "Doe";
        String email = "john.does@example.com";
        String company = "Example Corp";
        User.Role role = User.Role.USER_ROLE;

        when(userRepository.findByIdentifier(identifier)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        User createdUser = userService.createUser(identifier, password, fName, lName, email, company, role);

        assertNotNull(createdUser);
        assertEquals(identifier, createdUser.getIdentifier());
        assertEquals(encodedPassword, createdUser.getPassword());
        assertEquals(fName, createdUser.getFirstName());
        assertEquals(lName, createdUser.getLastName());
        assertEquals(email, createdUser.getEmail());
        assertEquals(company, createdUser.getCompany());
        assertEquals(role, createdUser.getRole());

        verify(userRepository, times(1)).save(createdUser);
    }*/

    @Test
    public void testCreateUser_DuplicateIdentifier() {
        String identifier = "existinguser";
        String email = "john.doe@example.com";

        when(userRepository.findByIdentifier(identifier)).thenReturn(Optional.of(new User()));

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(identifier, "password", "John", "Doe", email, "Example Corp", User.Role.USER_ROLE),
                "Expected createUser() to throw, but it didn't"
        );

        assertEquals("user.validation.identifier.exists", thrown.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testCreateUser_DuplicateEmail() {
        String identifier = "newuser";
        String email = "existingemail@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(identifier, "password", "John", "Doe", email, "Example Corp", User.Role.USER_ROLE),
                "Expected createUser() to throw, but it didn't"
        );

        assertEquals("user.validation.email.exists", thrown.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testLoadUserByUsername_Success() {
        String email = "john.doe@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testLoadUserByUsername_NotFound() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(email));
    }

    @Test
    public void testGetCurrentUser() {
        User user = new User();
        user.setIdentifier("currentuser");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContextHolder.setContext(securityContext);

        User currentUser = userService.getCurrentUser();

        assertNotNull(currentUser);
        assertEquals("currentuser", currentUser.getIdentifier());
    }
}
