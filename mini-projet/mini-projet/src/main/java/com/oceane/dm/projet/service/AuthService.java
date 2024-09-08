package com.oceane.dm.projet.service;

import com.oceane.dm.models.model.TwoFactorUser;
import com.oceane.dm.projet.security.JwtUtils;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Service
public class AuthService {

    private final SecretGenerator secretGenerator;
    private final QrGenerator qrGenerator;
    private CodeVerifier verifier;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    public AuthService(SecretGenerator secretGenerator, QrGenerator qrGenerator, AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService) {
        this.secretGenerator = secretGenerator;
        this.qrGenerator = qrGenerator;
        this.userService = userService;
        TimeProvider timeProvider = new SystemTimeProvider();
        DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA1);
        this.verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    /**
     *
     * @return String SecretKey for account
     */
    public String generateSecretKey() {
        return secretGenerator.generate();
    }

    /**
     * Méthode permettant de générer les informations contenu dans le Qrcode
     * @param secret clé secret du compte pour la double auth
     * @param account information d'authentification du compte
     * @return QrCode a scanner par une application
     * @throws QrGenerationException
     */
    public String getQRCode(String secret, String account) throws QrGenerationException {
        QrData data = new QrData.Builder()
                .label(account)
                .secret(secret)
                .issuer("MyApp")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();
        return getDataUriForImage(
                qrGenerator.generate(data),
                qrGenerator.getImageMimeType()
        );
    }

    /**
     * Methode permettant de vérifier la validité du code saisi par l'utilisateur
     * @param secret clé secrete associé au compte
     * @param code code saisi par l'utilisateur
     * @return true si code correct False sinon
     */
    public boolean verifyCode(String secret, String code) {
        return verifier.isValidCode(secret, code);
    }

    /**
     * Methode permettant la verification des information de compte saisi par l'utilisateur
     * @param email
     * @param password
     * @return {2FA_REQUIRED} si la 2Fa est active pour le compte
     * sinon val qui represente le JwtTOken générer
     */
    public String login(String email, String password) {

        LOGGER.info("AuthService Login request for user {}", email);
        someMethod();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        System.out.println("bonjour");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        TwoFactorUser user = (TwoFactorUser) authentication.getPrincipal();
        System.out.println("user.isAuth2F() :"+user.isAuth2Fa());
        if (user.isAuth2Fa()){
            return "2FA_REQUIRED";
        }else{
            String val =jwtUtils.generateJwtToken(authentication);
            System.out.println(val);
            return val;
        }
    }

    /*public String verify2FA(String email, String secret,String code) {
        TwoFactorUser user = (TwoFactorUser) userService.loadUserByUsername(email);
        if (verifyCode(secret, code)) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return jwtUtils.generateJwtToken(authentication);
        } else {
            //throw new IllegalArgumentException("Invalid TOTP code");
            return "Invalid TOTP code";
        }
    }*/


    /**
     *
     * @param email email du compte
     * @param secret clé secrète du compte
     * @param code code saisi par l'utilisateur
     * @return le JwtTOken générer si le code saisi est correcte ,
     * sinon "Invalid TOTP code"
     */
    public String verify2FA(String email, String secret, String code) {
        TwoFactorUser user = (TwoFactorUser) userService.loadUserByUsername(email);
        if (user == null) {
            return "User not found";
        }
        System.out.println("User found: " + user); // Log pour vérifier l'utilisateur
        boolean isCodeValid = verifyCode(secret, code);
        System.out.println("Code validity: " + isCodeValid); // Log pour vérifier la validité du code
        if (isCodeValid) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return jwtUtils.generateJwtToken(authentication);
        } else {
            return "Invalid TOTP code";
        }
    }

    /*public String verify2FA(String email, String secret, String code) {
        TwoFactorUser user = (TwoFactorUser) userService.loadUserByUsername(email);
        if (user == null) {
            // Gérer le cas où l'utilisateur est null
            return "User not found";
        }
        if (verifyCode(secret, code)) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return jwtUtils.generateJwtToken(authentication);
        } else {
            return "Invalid TOTP code";
        }
    }*/


    public void setVerifier(CodeVerifier verifier) {
        this.verifier = verifier;
    }


    public void someMethod() {
        userService.printAllUsersToConsole();
    }
}