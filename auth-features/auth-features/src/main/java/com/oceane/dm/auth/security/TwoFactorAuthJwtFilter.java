package com.oceane.dm.auth.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Qualifier("twoFactorAuthJwtFilter")
public class TwoFactorAuthJwtFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TwoFactorAuthJwtFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public TwoFactorAuthJwtFilter(JwtUtils jwtUtils, @Qualifier("twoFactorUserService") UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (!request.getRequestURI().startsWith("/api/auth2Fa") && !request.getRequestURI().startsWith("/api/users2Fa")) {
                filterChain.doFilter(request, response);
                return;
            }
            LOGGER.info("TwoJwtFilter applied for URI: {}", request.getRequestURI());
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException e) {
            LOGGER.error("JWT token is expired: {}", e.getMessage());
            response.setHeader("X-Expired", "true");
        } catch (Exception e) {
            LOGGER.error("Cannot set user authentication: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER_PREFIX)) {
            return headerAuth.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}

/*
@Component
public class TwoFactorAuthJwtFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TwoFactorAuthJwtFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public TwoFactorAuthJwtFilter(JwtUtils jwtUtils, @Qualifier("twoFactorUserService") UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Filtre de validation du token.
     * Renvoie un header spécifique si le token est expiré pour que le client le gère.
     *
     * @param request     requête reçue
     * @param response    réponse envoyée
     * @param filterChain chaine de filtres
     */
    /*@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException e) {
            LOGGER.error("JWT token is expired: {}", e.getMessage());
            response.setHeader("X-Expired", "true");
        } catch (Exception e) {
            LOGGER.error("Cannot set user authentication: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }*/
    /*@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        LOGGER.info("TwoFactorAuthJwtFilter applied for URI: {}", request.getRequestURI());
        String jwt = parseJwt(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }


    /**
     * Récupère le token depuis le header 'Authorization'.
     *
     * @param request requête à analyser
     * @return token de la requête
     */
    /*private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER_PREFIX)) {
            return headerAuth.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}*/
/*
@Component
public class TwoFactorAuthJwtFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TwoFactorAuthJwtFilter.class);

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public TwoFactorAuthJwtFilter(JwtUtils jwtUtils, @Qualifier("twoFactorUserService") UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        LOGGER.info("TwoFactorAuthJwtFilter applied for URI: {}", request.getRequestURI());
        String jwt = parseJwt(request);

        if (jwt != null) {
            LOGGER.info("JWT Token: {}", jwt);
            if (jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                LOGGER.info("JWT Token is valid. Username: {}", username);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                LOGGER.info("UserDetails loaded. Username: {}", userDetails.getUsername());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                LOGGER.warn("Invalid JWT Token");
            }
        } else {
            LOGGER.warn("JWT Token is null or invalid");
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        LOGGER.info("Authorization Header: {}", headerAuth);
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}*/

