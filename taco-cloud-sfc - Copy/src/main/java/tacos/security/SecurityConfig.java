package tacos.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import tacos.User;
import tacos.data.UserRepository;

/**
 * Security configuration for the application, defining authentication
 * and authorization rules.
 */
@Configuration
public class SecurityConfig {

  /**
   * Creates and returns a PasswordEncoder bean using BCrypt.
   * BCrypt is a secure algorithm for hashing passwords.
   * @return a BCryptPasswordEncoder instance
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Defines a custom UserDetailsService to load user details from the repository.
   * It retrieves user information from the database and returns it as a UserDetails object.
   * @param userRepo the UserRepository used to fetch user data
   * @return a UserDetailsService instance
   */
  @Bean
  public UserDetailsService userDetailsService(UserRepository userRepo) {
    return username -> {
      User user = userRepo.findByUsername(username);  // Fetch user by username
      if (user != null) {
        return user;  // Return the user if found
      }
      throw new UsernameNotFoundException(
              "User '" + username + "' not found");  // Throw exception if not found
    };
  }

  /**
   * Configures the HTTP security for the application, including authorization
   * rules, form login, and logout.
   * @param http the HttpSecurity object used to configure security
   * @return a SecurityFilterChain bean that applies the configuration
   * @throws Exception if an error occurs while configuring security
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
            .authorizeRequests()
            // Define authorization rules: users must have the "USER" role for /design and /orders pages
            .mvcMatchers("/design", "/orders").hasRole("USER")
            // Allow all other requests
            .anyRequest().permitAll()

            .and()
            // Configure the login page
            .formLogin()
            .loginPage("/login")

            .and()
            // Configure the logout behavior
            .logout()
            .logoutSuccessUrl("/")  // Redirect to home page after logout

            // Allow access to the H2-Console for debugging purposes
            .and()
            .csrf()
            .ignoringAntMatchers("/h2-console/**")  // Disable CSRF protection for H2-Console

            // Allow pages to be loaded in frames from the same origin (necessary for H2-Console)
            .and()
            .headers()
            .frameOptions()
            .sameOrigin()

            .and()
            .build();  // Apply the configured security settings
  }

}
