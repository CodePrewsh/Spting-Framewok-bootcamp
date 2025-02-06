package com.jwt.tut.config;

// Importing required classes and annotations
import com.jwt.tut.filter.JwtAuthFilter;
import com.jwt.tut.service.UserDetailsServiceImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Mark this class as a configuration class for Spring Security
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Custom user details service to load user-specific data
    private final UserDetailsServiceImp userDetailsServiceImp;

    // Custom JWT authentication filter to validate tokens
    private final JwtAuthFilter jwtAuthFilter;

    // Constructor to initialize dependencies
    public SecurityConfig(UserDetailsServiceImp userDetailsServiceImp, JwtAuthFilter jwtAuthFilter) {
        this.userDetailsServiceImp = userDetailsServiceImp;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * @param http the HttpSecurity object for configuring web-based security
     * @return a configured SecurityFilterChain bean
     * @throws Exception if any error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Disable CSRF protection (not recommended for stateful applications)
                .csrf(AbstractHttpConfigurer::disable)

                // Define authorization rules for endpoints
                .authorizeHttpRequests(
                        req -> req
                                // Allow unrestricted access to login and registration endpoints
                                .requestMatchers("/login/**", "/register/**").permitAll()

                                // Restrict access to admin-only endpoints to users with "ADMIN" authority
                                .requestMatchers("/admin_only/**").hasAnyAuthority("ADMIN")

                                // Require authentication for all other requests
                                .anyRequest().authenticated()
                )
                // Set the custom user details service for authentication
                .userDetailsService(userDetailsServiceImp)

                // Configure the session management policy to stateless (suitable for JWT-based authentication)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Add the JWT authentication filter before the UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // Build and return the SecurityFilterChain
                .build();
    }

    /**
     * Provides a PasswordEncoder bean for encrypting passwords.
     *
     * @return a BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides an AuthenticationManager bean for managing authentication processes.
     *
     * @param authConfig the AuthenticationConfiguration object for retrieving the authentication manager
     * @return the configured AuthenticationManager
     * @throws Exception if any error occurs during configuration
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
