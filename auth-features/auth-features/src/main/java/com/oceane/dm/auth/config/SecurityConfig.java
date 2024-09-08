package com.oceane.dm.auth.config;

import com.oceane.dm.auth.security.JwtFilter;
import com.oceane.dm.auth.security.TwoFactorAuthJwtFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

/**
 * Configuration class used to configure the spring security module.
 */

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final UserDetailsService twoFactorUserService;
    private final JwtFilter simpleAuthJwtFilter;
    private final TwoFactorAuthJwtFilter twoFactorAuthJwtFilter;
    private final List<String> corsOrigins;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(
            @Qualifier("userService") UserDetailsService userDetailsService,
            @Qualifier("twoFactorUserService") UserDetailsService twoFactorUserService,
            @Qualifier("simpleAuthJwtFilter") JwtFilter simpleAuthJwtFilter,
            @Qualifier("twoFactorAuthJwtFilter") TwoFactorAuthJwtFilter twoFactorAuthJwtFilter,
            @Value("${app.cors.origins}") List<String> corsOrigins,
            PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.twoFactorUserService = twoFactorUserService;
        this.simpleAuthJwtFilter = simpleAuthJwtFilter;
        this.twoFactorAuthJwtFilter = twoFactorAuthJwtFilter;
        this.corsOrigins = corsOrigins;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(ahr -> ahr
                        .requestMatchers(
                                new AntPathRequestMatcher("/"),
                                new AntPathRequestMatcher("/js/**"),
                                new AntPathRequestMatcher("/css/**"),
                                new AntPathRequestMatcher("/img/**"),
                                new AntPathRequestMatcher("/assets/**"),
                                new AntPathRequestMatcher("/favicon.*"),
                                new AntPathRequestMatcher("/index.html")).permitAll()
                        .requestMatchers(
                                new AntPathRequestMatcher("/login"),
                                new AntPathRequestMatcher("/login-2fa"),
                                new AntPathRequestMatcher("/register2Fa"),
                                new AntPathRequestMatcher("/setup-2fa"),
                                new AntPathRequestMatcher("/about")).permitAll()
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/users/current"),
                                new AntPathRequestMatcher("/api/auth/login")).permitAll()
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/auth2Fa/register"),
                                new AntPathRequestMatcher("/api/auth2Fa/setup"),
                                new AntPathRequestMatcher("/api/users2Fa/current"),
                                new AntPathRequestMatcher("/api/auth2Fa/verify"),
                                new AntPathRequestMatcher("/api/auth2Fa/login")).permitAll()
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/users/**"),
                                new AntPathRequestMatcher("/api/auth/**")).authenticated()
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/users2Fa/**"),
                                new AntPathRequestMatcher("/api/auth2Fa/**")).authenticated()
                        .anyRequest().authenticated())
                .exceptionHandling(eh -> eh.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(simpleAuthJwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(twoFactorAuthJwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManagerBean() {
        return new ProviderManager(List.of(simpleAuthProvider(), twoFactorAuthProvider()));
    }


    @Bean
    public AuthenticationProvider simpleAuthProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationProvider twoFactorAuthProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(twoFactorUserService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(corsOrigins);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
