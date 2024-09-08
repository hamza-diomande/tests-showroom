package com.oceane.dm.projet.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Classe utilitaire pour la gestion des tokens.
 */
@Component
public class JwtUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    /**
     * Secret pour sécuriser le token.
     */
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    /**
     * Temps de validité du token.
     */
    @Value("${app.jwtExpiration}")
    private int jwtExpiration;

    /**
     * Permet de générer un token en fonction d'un utilisateur.
     *
     * @param authentication l'utilisateur pour qui le token sera créé
     * @return le token
     */
    public String generateJwtToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return Jwts.builder()
                   .setSubject(userPrincipal.getUsername())
                   .setIssuedAt(new Date())
                   .setExpiration(new Date((new Date()).getTime() + jwtExpiration))
                   .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)))
                   .compact();
    }

    /**
     * Récupère l'adresse mail du token.
     *
     * @param token token à analyser
     * @return l'adresse mail de l'utilisateur
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                   .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)))
                   .build()
                   .parseSignedClaims(token)
                   .getBody()
                   .getSubject();
    }

    /**
     * Permet de valider un token.
     *
     * @param authToken le token à valider
     * @return vrai si le token est valide, faux sinon
     * @throws ExpiredJwtException permet de récupérer le fait que le token soit expiré
     */
    public boolean validateJwtToken(String authToken) throws ExpiredJwtException {
        try {
            Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)))
                .build()
                .parseSignedClaims(authToken);
            return true;
        } catch (SecurityException e) {
            LOGGER.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
