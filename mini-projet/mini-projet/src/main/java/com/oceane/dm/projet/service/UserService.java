package com.oceane.dm.projet.service;


import com.oceane.dm.models.model.TwoFactorUser;
import com.oceane.dm.models.repository.TwoFactorUserRepository;
import com.oceane.dm.projet.model.dto.TwoFactorUserDTO;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@Primary
public class UserService implements UserDetailsService {

    private final TwoFactorUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final CaptchaService captchaService;

    public UserService(TwoFactorUserRepository userRepository, PasswordEncoder passwordEncoder, CaptchaService captchaService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.captchaService=captchaService;
    }

    // Prevents sonarcube from signaling the "String literals should not be duplicated" error for a temporary test code
    @SuppressWarnings("java:S1192")
    @PostConstruct
    public void createAdmin() {
        // fake user allowing to log in to the basic application
        if (userRepository.findByIdentifier("admin").isEmpty()) {
            createAdminUser("admin", "admin", "admin", "admin", "admin@admin.com", "", TwoFactorUser.Role.ADMIN_ROLE);
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
    public TwoFactorUser createAdminUser(String identifier, String password, String fName, String lName, String email, String company, TwoFactorUser.Role role) {
        if (userRepository.findByIdentifier(identifier).isPresent()) {
            throw new IllegalArgumentException("user.validation.identifier.exists");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("user.validation.email.exists");
        }

        TwoFactorUser user = new TwoFactorUser();
        user.setIdentifier(identifier);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(fName);
        user.setLastName(lName);
        user.setEmail(email);
        user.setAuth2Fa(false);
        user.setCompany(company);
        user.setRole(role);
        return userRepository.save(user);
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
    public TwoFactorUser createUser(String identifier, String password, String fName, String lName, String email, String company, TwoFactorUser.Role role) {


        if (userRepository.findByIdentifier(identifier).isPresent()) {
            throw new IllegalArgumentException("user.validation.identifier.exists");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("user.validation.email.exists");
        }

        TwoFactorUser user = new TwoFactorUser();
        user.setIdentifier(identifier);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(fName);
        user.setLastName(lName);
        user.setEmail(email);
        user.setCompany(company);
        user.setRole(role);
        return userRepository.save(user);
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
     * @param captchaAnswer
     * @param captchaSession
     * @return the new user
     * @throws IllegalArgumentException when identifier or email already exists
     */
    public TwoFactorUser createUser(String identifier, String password, String fName, String lName, String email, String company, TwoFactorUser.Role role, String captchaSession, String captchaAnswer) {

        captchaService.checkCaptcha(captchaSession, captchaAnswer);

        if (userRepository.findByIdentifier(identifier).isPresent()) {
            throw new IllegalArgumentException("user.validation.identifier.exists");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("user.validation.email.exists");
        }

        TwoFactorUser user = new TwoFactorUser();
        user.setIdentifier(identifier);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(fName);
        user.setLastName(lName);
        user.setEmail(email);
        user.setCompany(company);
        user.setRole(role);
        return userRepository.save(user);
    }

    public TwoFactorUser updateUser(TwoFactorUser user,String captchaSession, String captchaAnswer){
        captchaService.checkCaptcha(captchaSession, captchaAnswer);
        Optional<TwoFactorUser> optionalTwoFactorUser = userRepository.findById(user.getId());
        if (!optionalTwoFactorUser.isPresent()){
            throw new IllegalArgumentException("l'utilisateur avec ID "+ user.getId() + " n'a pas été trouvé");
        }
        TwoFactorUser oldUser = optionalTwoFactorUser.get();
        oldUser.setCompany(user.getCompany());
        oldUser.setEmail(user.getEmail());
        oldUser.setIdentifier(user.getIdentifier());
        oldUser.setFirstName(user.getFirstName());
        oldUser.setLastName(user.getLastName());
        oldUser.setRole(user.getRole());
        return userRepository.save(oldUser);
    }


    public TwoFactorUser updateis2Fa(TwoFactorUser user)  {
        // Vérifier si l'utilisateur existe
        Optional<TwoFactorUser> optionalUser = userRepository.findById(user.getId());

        if (!optionalUser.isPresent()) {
            // Si l'utilisateur n'existe pas, lever une exception personnalisée
            throw new IllegalArgumentException("User with ID " + user.getId() + " not found.");
        }

        // Récupérer l'utilisateur
        TwoFactorUser oldUser = optionalUser.get();

        // Générer la clé secrète et activer l'authentification à deux facteurs
        oldUser.setSecret(user.getSecret());
        oldUser.setAuth2Fa(user.isAuth2Fa());

        // Mettre à jour l'utilisateur dans la base de données

        // Retourner l'utilisateur mis à jour
        return userRepository.save(oldUser);
    }

    @Transactional
    public Optional<TwoFactorUser> updateUser(Long userId, TwoFactorUserDTO userDTO) {
        // Vérification du CAPTCHA
        captchaService.checkCaptcha(userDTO.getCaptchaSession(), userDTO.getCaptchaAnswer());

        // Recherche et mise à jour de l'utilisateur
        return userRepository.findById(userId).map(existingUser -> {
            // Mise à jour conditionnelle des champs
            updateIfChanged(existingUser::setRole, existingUser.getRole().name(), userDTO.getRole(), TwoFactorUser.Role::valueOf);
            updateIfChanged(existingUser::setLastName, existingUser.getLastName(), userDTO.getLastName());
            updateIfChanged(existingUser::setFirstName, existingUser.getFirstName(), userDTO.getFirstName());
            updateIfChanged(existingUser::setSecret, existingUser.getSecret(), userDTO.getSecret());
            updateIfChanged(existingUser::setAuth2Fa, existingUser.isAuth2Fa(), userDTO.isAuth2Fa());
            updateIfChanged(existingUser::setEmail, existingUser.getEmail(), userDTO.getEmail());
            updateIfChanged(existingUser::setCompany, existingUser.getCompany(), userDTO.getCompany());

            // Persister les changements dans la base de données
            return userRepository.save(existingUser);
        });
    }

    private <T> void updateIfChanged(Consumer<T> setter, T existingValue, T newValue) {
        if (newValue != null && !newValue.equals(existingValue)) {
            setter.accept(newValue);
        }
    }

    private <T, U> void updateIfChanged(Consumer<T> setter, U existingValue, U newValue, Function<U, T> converter) {
        if (newValue != null && !newValue.equals(existingValue)) {
            setter.accept(converter.apply(newValue));
        }
    }

    /**
     * Supprime un utilisateur par ID.
     *
     * @param userId l'ID de l'utilisateur à supprimer
     * @return true si l'utilisateur a été supprimé, false sinon
     */
    public boolean deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        } else {
            return false;
        }
    }

    public TwoFactorUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (TwoFactorUser) auth.getPrincipal();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    // Nouvelle méthode pour obtenir la liste de tous les utilisateurs
    public List<TwoFactorUser> getAllUsers() {
        return userRepository.findAll();
    }



    // Nouvelle méthode pour afficher les utilisateurs dans la console
    public void printAllUsersToConsole() {
        List<TwoFactorUser> users = getAllUsers();

        if (users.isEmpty()) {
            System.out.println("Aucun utilisateur n'a été trouvé.");
        } else {
            for (TwoFactorUser user : users) {
                System.out.println(user);
            }
        }
    }

    public Optional<TwoFactorUser> getUserById(Long userId) {
        return userRepository.findById(userId);
    }



}