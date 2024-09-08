package com.oceane.dm.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.oceane.dm.model.User;
import com.oceane.dm.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Prevents sonarcube from signaling the "String literals should not be duplicated" error for a temporary test code
    @java.lang.SuppressWarnings("java:S1192")
    @PostConstruct
    public void createAdmin() {
        // fake user allowing to log in to the basic application
        if (userRepository.findByIdentifier("admin").isEmpty()) {
            createUser("admin", "admin", "admin", "admin", "admin@admin.com", "", User.Role.ADMIN_ROLE);
        }
    }

    /**
     * Create a user.
     *
     * @param identifier the identifier
     * @param password   the password
     * @param fName      the first name
     * @param lName      the last name
     * @param email      the email
     * @param company    the company
     * @param role       the role
     * @return the new user
     * @throws IllegalArgumentException when identifier or email already exists
     */
    public User createUser(String identifier, String password, String fName, String lName, String email, String company, User.Role role) {
        if (userRepository.findByIdentifier(identifier).isPresent()) {
            throw new IllegalArgumentException("user.validation.identifier.exists");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("user.validation.email.exists");
        }

        User user = new User();
        user.setIdentifier(identifier);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(fName);
        user.setLastName(lName);
        user.setEmail(email);
        user.setCompany(company);
        user.setRole(role);
        return userRepository.save(user);
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }
}
