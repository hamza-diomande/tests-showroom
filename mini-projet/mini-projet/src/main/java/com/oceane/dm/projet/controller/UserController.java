package com.oceane.dm.projet.controller;



import com.oceane.dm.projet.model.RegisterRequest;
import com.oceane.dm.projet.model.dto.TwoFactorUserDTO;
import com.oceane.dm.projet.service.AuthService;
import com.oceane.dm.projet.service.UserService;
import com.oceane.dm.models.model.TwoFactorUser;
import com.oceane.dm.models.repository.TwoFactorUserRepository;
import dev.samstevens.totp.exceptions.QrGenerationException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final TwoFactorUserRepository userRepository;

    public UserController(UserService userService, AuthService authService, TwoFactorUserRepository userRepository) {
        this.userService = userService;
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @GetMapping("/current")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public TwoFactorUser getCurrentUser() {
        System.out.println("testons current");
        return userService.getCurrentUser();
    }

    @PostMapping("/register")
    public ResponseEntity<TwoFactorUser> registerUser(@Valid @RequestBody TwoFactorUser user) {
        if (user.getIdentifier() == null || user.getFirstName() == null || user.getLastName() == null ||
                user.getEmail() == null || user.getPassword() == null || user.getCompany() == null) {
            throw new IllegalArgumentException("Toutes les entrées obligatoires doivent être présentes.");
        }
        // Vérifier que le mot de passe contient au moins 8 caractères
        if (user.getPassword().length() < 8) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caractères.");
        }
        user.setSecret(null);
        user.setAuth2Fa(false);
        TwoFactorUser registeredUser = userService.createUser(user.getIdentifier(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getCompany(), user.getRole());
        System.out.println(registeredUser.getId());
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    /**
     * Methode
     * @param registerRequest
     * @return
     */
   @PostMapping("/register-user")
    public ResponseEntity<TwoFactorUser> registerUser(@RequestBody @Valid RegisterRequest registerRequest) {
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
        TwoFactorUser registeredUser = userService.createUser(registerRequest.getIdentifier(), registerRequest.getPassword(), registerRequest.getFirstName(), registerRequest.getLastName(), registerRequest.getEmail(), registerRequest.getCompany(), registerRequest.getRole(), registerRequest.getCaptchaSession(), registerRequest.getCaptchaAnswer());
        System.out.println(registeredUser.getId());
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

   @PostMapping("/activate-2fa")
    public ResponseEntity<Map<String, String>>  activate2FA(@RequestParam Long userId) throws QrGenerationException {
        /* TwoFactorUser user = null;
        try {
        if (userRepository.findById(userId).isPresent()){
            user = userRepository.findById(userId).get();
            user.setSecret(authService.generateSecretKey());
            user.setAuth2Fa(true);
            userService.updateis2Fa(user);

        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String qrCode = authService.getQRCode(user.getSecret(), user.getEmail());

        return ResponseEntity.ok(Map.of("secret", user.getSecret(), "qrCode", qrCode));
    } catch (QrGenerationException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }*/
       TwoFactorUser user = userRepository.findById(userId).orElse(null);

       if (user == null) {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }

       // Generate secret key and QR code
       String secret = authService.generateSecretKey();
       String qrCode = authService.getQRCode(secret, user.getEmail());

       // Update user with the generated secret key and enable 2FA
       user.setSecret(secret);
       user.setAuth2Fa(true);
       userService.updateis2Fa(user);

       // Return response with secret and QR code
       if (secret != null && qrCode != null) {
           return ResponseEntity.ok(Map.of("secret", secret, "qrCode", qrCode));
       } else {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
       }
    }

    @PostMapping("/deactivate-2fa")
    public ResponseEntity<TwoFactorUser> deactivate2FA(@RequestParam Long userId) {
       /* TwoFactorUser user = null;
        if (userRepository.findById(userId).isPresent()){
            user = userRepository.findById(userId).get();
            user.setSecret(null);
            user.setAuth2Fa(false);
            userService.updateis2Fa(user);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);*/
        Optional<TwoFactorUser> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            TwoFactorUser user = userOptional.get();
            user.setSecret(null);
            user.setAuth2Fa(false);
            userService.updateis2Fa(user);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/update-user")
    public ResponseEntity<TwoFactorUser> updateUser(@RequestParam Long userId, @Valid @RequestBody TwoFactorUserDTO userDTO) {
        Optional<TwoFactorUser> updatedUser = userService.updateUser(userId, userDTO);

        return updatedUser
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @GetMapping
    public List<TwoFactorUser> getAllUsers(){
        return userService.getAllUsers();
    }

    /**
     * Supprime un utilisateur par ID.
     *
     * @param userId l'ID de l'utilisateur à supprimer
     * @return une réponse HTTP
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        boolean deleted = userService.deleteUser(userId);

        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }

    /**
     * Récupère un utilisateur par son ID.
     *
     * @param userId l'ID de l'utilisateur à récupérer
     * @return les informations de l'utilisateur, ou une réponse 404 si l'utilisateur n'est pas trouvé
     */
    @GetMapping("/{userId}")
    public ResponseEntity<TwoFactorUser> getUserById(@PathVariable Long userId) {
        Optional<TwoFactorUser> userOptional = userService.getUserById(userId);

        // Vérifier si l'utilisateur est présent
        if (userOptional.isPresent()) {
            return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}