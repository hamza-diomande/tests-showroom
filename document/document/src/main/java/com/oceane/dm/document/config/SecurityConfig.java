package com.oceane.dm.document.config;

import com.oceane.dm.document.security.JwtFilter;
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
    private final JwtFilter jwtFilter;
    private final List<String> corsOrigins;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(
            UserDetailsService userDetailsService,
            JwtFilter jwtFilter,
            @Value("${app.cors.origins}") List<String> corsOrigins,
            PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
        this.corsOrigins = corsOrigins;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf((AbstractHttpConfigurer::disable))
                .cors(Customizer.withDefaults())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(ahr -> ahr
                        .requestMatchers(
                                antMatcher("/"),
                                antMatcher("/js/**"),
                                antMatcher("/css/**"),
                                antMatcher("/img/**"),
                                antMatcher("/assets/**"),
                                antMatcher("/favicon.*"),
                                antMatcher("/index.html")).permitAll()  // ressources statiques web
                        .requestMatchers(
                                antMatcher("/login"),
                                antMatcher("/about"),
                                antMatcher("/users"),
                                antMatcher("/dummy")).permitAll() // routes web
                        .requestMatchers(
                                antMatcher("/api/auth/login"),
                                antMatcher("/api/users/**"),
                                antMatcher("/api/genDoc/**"),
                                antMatcher("/h2-console"),
                                antMatcher("/error")).permitAll() // api publiques
                        .requestMatchers(
                                antMatcher("/api/**")).authenticated() // api privées
                        .anyRequest().authenticated()) // tout le reste

                .exceptionHandling(eh -> eh.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() {
        return new ProviderManager(List.of(authenticationProvider()));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * @return the cors configuration
     */
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
