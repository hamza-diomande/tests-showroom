package com.oceane.dm.projet.config;

import com.oceane.dm.projet.security.JwtFilter;
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
    private final String phpCaptcheckUrl;

    public SecurityConfig(
            UserDetailsService userDetailsService,
            JwtFilter jwtFilter,
            @Value("${app.cors.origins}") List<String> corsOrigins,
            PasswordEncoder passwordEncoder,
    @Value("${app.phpcaptcheck.url}")String phpCaptcheckUrl) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
        this.corsOrigins = corsOrigins;
        this.passwordEncoder = passwordEncoder;
        this.phpCaptcheckUrl = phpCaptcheckUrl;
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
                                antMatcher("/new-user"),
                                antMatcher("/users"),
                                antMatcher("/edit-user"),
                                antMatcher("/about"),
                                antMatcher("/dummy")).permitAll() // routes web
                        .requestMatchers(
                                antMatcher("/api/application/**"),
                                antMatcher("/api/auth/**"),
                                antMatcher("/api/dummy"),
                                antMatcher("/api/users/register"),
                                antMatcher("/api/users/register-user"),
                                antMatcher("/api/users/**"),
                                antMatcher("/h2-console"),
                                antMatcher("/error")).permitAll() // api publiques
                        .requestMatchers(
                                antMatcher("/api/users/deactivate-2fa"),
                                antMatcher("/api/users/activate-2fa"),
                                antMatcher("/api/**")).authenticated() // api privées
                        .anyRequest().authenticated()) // tout le reste

                .exceptionHandling(eh -> eh.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self' 'unsafe-eval' " + phpCaptcheckUrl + "; font-src 'self' fonts.gstatic.com data:; style-src 'self' 'unsafe-inline' fonts.googleapis.com; img-src 'self' " + phpCaptcheckUrl + " data:; form-action 'self'; object-src 'none'; frame-ancestors 'none'; block-all-mixed-content"))
                )
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
