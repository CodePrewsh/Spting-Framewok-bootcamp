package tacos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Marks this class as a configuration class for Spring Security
@Configuration
// Enables Spring Security in the application
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  // Injects a custom UserDetailsService for user authentication
  @Autowired
  private UserDetailsService userDetailsService;

  /**
   * Configures security for HTTP requests.
   *
   * @param http HttpSecurity object to configure web-based security
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
            .authorizeRequests()  // Starts configuring request-based authorization
            // Requires users with 'USER' role to access these endpoints
            .antMatchers("/design", "/orders").access("hasRole('USER')")
            // Allows unrestricted access to all other endpoints
            .antMatchers("/**").access("permitAll")

            .and()
            .formLogin()  // Enables form-based login
            .loginPage("/login")  // Specifies the login page URL

            .and()
            .logout()  // Enables logout functionality
            .logoutSuccessUrl("/")  // Redirects to homepage on successful logout

            .and()
            .csrf()  // Enables CSRF protection
            .ignoringAntMatchers("/h2-console/**")  // Disables CSRF for H2 console access

            // Configures headers to allow H2-Console in a frame (needed for H2 database console)
            .and()
            .headers()
            .frameOptions()
            .sameOrigin();  // Allows pages to be displayed in frames from the same origin
  }

  /**
   * Provides a PasswordEncoder bean to encode passwords with BCrypt hashing.
   *
   * @return a BCryptPasswordEncoder instance
   */
  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Configures the authentication manager with the custom user details service and password encoder.
   *
   * @param auth AuthenticationManagerBuilder to configure authentication
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth)
          throws Exception {
    auth
            .userDetailsService(userDetailsService)  // Uses custom UserDetailsService for authentication
            .passwordEncoder(encoder());  // Sets BCryptPasswordEncoder for password encoding
  }

}
