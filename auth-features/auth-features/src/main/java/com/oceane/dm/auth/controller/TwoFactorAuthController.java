package com.oceane.dm.auth.controller;

import com.oceane.dm.models.model.TwoFactorUser;
import com.oceane.dm.auth.model.exception.UserNotFoundException;
import com.oceane.dm.models.repository.TwoFactorUserRepository;
import com.oceane.dm.auth.services.TwoFactorAuthService;
import com.oceane.dm.auth.services.TwoFactorUserService;
import dev.samstevens.totp.exceptions.QrGenerationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth2Fa")
public class TwoFactorAuthController {

    @Autowired
    private TwoFactorAuthService twoFactorAuthService;
    @Autowired
    private TwoFactorUserService twoFactorUserService;
    @Autowired
    private TwoFactorUserRepository twoFactorUserRepository;

    @PostMapping("/register")
    public ResponseEntity<TwoFactorUser> registerUser( @Valid @RequestBody TwoFactorUser user) {
        if (user.getIdentifier() == null || user.getFirstName() == null || user.getLastName() == null ||
                user.getEmail() == null || user.getPassword() == null || user.getCompany() == null) {
            throw new IllegalArgumentException("Toutes les entrées obligatoires doivent être présentes.");
        }
        // Vérifier que le mot de passe contient au moins 8 caractères
        if (user.getPassword().length() < 8) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caractères.");
        }
        user.setSecret(twoFactorAuthService.generateSecretKey());
        user.setAuth2Fa(true);
        TwoFactorUser registeredUser = twoFactorUserService.registerUser(user);
        System.out.println(registeredUser.getId());
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/setup")
    public ResponseEntity<Map<String, String>> setupTwoFactorAuth(@RequestBody Map<String, String> request) {
        String account = request.get("account");

        try {
            TwoFactorUser user = twoFactorUserRepository.findByEmail(account)
                    .orElseThrow(() -> new UserNotFoundException("Aucun utilisateur trouvé avec l'e-mail : " + account));

            String secret = user.getSecret();

            // Si l'utilisateur n'a pas encore de clé secrète, en générer une nouvelle
            if (secret == null || secret.isEmpty()) {
                secret = twoFactorAuthService.generateSecretKey();
                user.setSecret(secret);
                twoFactorUserRepository.save(user);
            }

            // Générer le code QR
            String qrCode = twoFactorAuthService.getQRCode(secret, account);
            System.out.println(qrCode);

            // Retourner la clé secrète et le code QR
            return ResponseEntity.ok(Map.of("secret", secret, "qrCode", qrCode));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (QrGenerationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/verify")
    public ResponseEntity<String> verifyTwoFactorAuth(@RequestBody Map<String, String> request) {
        String secret = request.get("secret");
        String code = request.get("totpCode");
        String email = request.get("email");
        System.out.println("secret : " +secret);
        System.out.println("code : " +code);
        System.out.println("email : " + email);
        String isVerified = twoFactorAuthService.verify2FA(email,secret, code);
        return ResponseEntity.ok(isVerified);
    }




    /**
     * Login a user to get a jwt.
     *
     * @param loginRequest the login request
     * @return the jwt
     */
  /*  @PostMapping("/login")
    public String login(@RequestBody @Valid LoginRequest loginRequest) {
        System.out.println("test");
        System.out.println("loginRequest.getEmail() :"+ loginRequest.getEmail());
        System.out.println("loginRequest.getPassword() :"+ loginRequest.getPassword());
        return twoFactorAuthService.login(
                loginRequest.getEmail(),
                loginRequest.getPassword());
    }*/
}
