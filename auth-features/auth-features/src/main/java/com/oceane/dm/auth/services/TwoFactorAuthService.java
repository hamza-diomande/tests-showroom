package com.oceane.dm.auth.services;

import com.oceane.dm.models.model.TwoFactorUser;
import com.oceane.dm.auth.security.JwtUtils;
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
public class TwoFactorAuthService {

    private final SecretGenerator secretGenerator;
    private final QrGenerator qrGenerator;
    private final CodeVerifier verifier;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final TwoFactorUserService twoFactorUserService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TwoFactorAuthService.class);

    @Autowired
    public TwoFactorAuthService(SecretGenerator secretGenerator, QrGenerator qrGenerator, AuthenticationManager authenticationManager, JwtUtils jwtUtils, TwoFactorUserService twoFactorUserService) {
        this.secretGenerator = secretGenerator;
        this.qrGenerator = qrGenerator;
        this.twoFactorUserService = twoFactorUserService;
        TimeProvider timeProvider = new SystemTimeProvider();
        DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA1);
        this.verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public String generateSecretKey() {
        return secretGenerator.generate();
    }

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

    public boolean verifyCode(String secret, String code) {
        return verifier.isValidCode(secret, code);
    }

    public String login(String email, String password) {

        LOGGER.info("TwoFactorAuthService Login request for user {}", email);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        TwoFactorUser user = (TwoFactorUser) authentication.getPrincipal();
        System.out.println("user.isAuth2F() :"+user.isAuth2Fa());
        if (user.isAuth2Fa()){
            return "2FA_REQUIRED";
        }else{
            return jwtUtils.generateJwtToken(authentication);
        }
    }

    public String verify2FA(String email, String secret,String code) {
        TwoFactorUser user = (TwoFactorUser) twoFactorUserService.loadUserByUsername(email);
        if (verifyCode(secret, code)) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return jwtUtils.generateJwtToken(authentication);
        } else {
            //throw new IllegalArgumentException("Invalid TOTP code");
            return "Invalid TOTP code";
        }
    }
}
