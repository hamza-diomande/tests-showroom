package com.oceane.dm.auth.services;

import com.oceane.dm.models.model.TwoFactorUser;
import com.oceane.dm.models.repository.TwoFactorUserRepository;
import jakarta.annotation.PostConstruct;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@Qualifier("twoFactorUserService")
public class TwoFactorUserService  implements UserDetailsService {

    private final TwoFactorUserRepository twoFactorUserRepository;
    private final PasswordEncoder passwordEncoder;

    public TwoFactorUserService(TwoFactorUserRepository twoFactorUserRepository,PasswordEncoder passwordEncoder) {
        this.twoFactorUserRepository=twoFactorUserRepository;
        this.passwordEncoder = passwordEncoder;
    }
    // Prevents sonarcube from signaling the "String literals should not be duplicated" error for a temporary test code
    @SuppressWarnings("java:S1192")
    @PostConstruct
    public void createAdmin() {
        // fake user allowing to log in to the basic application
        if (twoFactorUserRepository.findByIdentifier("admin").isEmpty()) {
            TwoFactorUser twoFactorUser = new TwoFactorUser();
            twoFactorUser.setIdentifier("admin");
            twoFactorUser.setPassword(passwordEncoder.encode("admin"));
            twoFactorUser.setFirstName("admin");
            twoFactorUser.setLastName("admin");
            twoFactorUser.setEmail("admin@admin.com");
            twoFactorUser.setAuth2Fa(false);
            twoFactorUser.setCompany("admin");
            twoFactorUser.setRole(TwoFactorUser.Role.ADMIN_ROLE);
            registerUser(twoFactorUser);
        }
    }

    public TwoFactorUser registerUser(@NotNull TwoFactorUser user) {
        if (twoFactorUserRepository.findByIdentifier(user.getIdentifier()).isPresent()) {
            throw new IllegalArgumentException("user2Fa.validation.identifier.exists");
        }
        if(!isValidEmailFormat(user.getEmail())){
            throw new IllegalArgumentException("user2Fa.validation.email.invalid_format");
        }
        if (twoFactorUserRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("user2Fa.validation.email.exists");
        }
        TwoFactorUser twoFactorUser = new TwoFactorUser();
        twoFactorUser.setIdentifier(user.getIdentifier());
        twoFactorUser.setPassword(passwordEncoder.encode(user.getPassword()));
        twoFactorUser.setFirstName(user.getFirstName());
        twoFactorUser.setLastName(user.getLastName());
        twoFactorUser.setEmail(user.getEmail());
        twoFactorUser.setAuth2Fa(user.isAuth2Fa());
        twoFactorUser.setCompany(user.getCompany());
        twoFactorUser.setRole(TwoFactorUser.Role.USER_ROLE);
        return twoFactorUserRepository.save(twoFactorUser);
    }

    public TwoFactorUser updateUser(TwoFactorUser user) {
        // Vérifiez si l'utilisateur existe dans la base de données
        if (twoFactorUserRepository.existsById(user.getId())) {
            // Mettez à jour l'utilisateur dans la base de données
            return twoFactorUserRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found with id: " + user.getId());
        }
    }

    private boolean isValidEmailFormat(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public TwoFactorUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (TwoFactorUser) auth.getPrincipal();
    }

   @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return twoFactorUserRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

}
