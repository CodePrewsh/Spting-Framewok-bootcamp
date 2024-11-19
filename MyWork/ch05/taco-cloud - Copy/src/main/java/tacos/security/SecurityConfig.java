package tacos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  // Inject UserDetailsService to manage user information
  @Autowired
  private UserDetailsService userDetailsService;

  // Configure HTTP security for the application
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
            .authorizeRequests()
            // Secure the "/design" and "/orders" endpoints to users with the "USER" role
            .antMatchers("/design", "/orders").access("hasRole('USER')")
            // Allow access to all other endpoints for everyone
            .antMatchers("/", "/**").access("permitAll")

            .and()
            .formLogin()
            // Define custom login page
            .loginPage("/login")

            .and()
            .logout()
            // Redirect to home page upon logout
            .logoutSuccessUrl("/")

            // Disable CSRF protection for H2 Console (debugging purposes)
            .and()
            .csrf()
            .ignoringAntMatchers("/h2-console/**")

            // Allow H2 Console to be embedded within frames from the same origin
            .and()
            .headers()
            .frameOptions()
            .sameOrigin();
  }

  /*
  Uncommented method: Configure authentication using UserDetailsService and PasswordEncoder.
  */

  @Bean
  public PasswordEncoder encoder() {
    // Provides a BCryptPasswordEncoder bean for password encoding
    return new BCryptPasswordEncoder();
  }

  // Configures authentication to use the userDetailsService and password encoder
  @Override
  protected void configure(AuthenticationManagerBuilder auth)
          throws Exception {

    auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(encoder());
  }

//
// In-Memory Authentication Example (uncomment if using in-memory authentication)
//
/*
  @Override
  protected void configure(AuthenticationManagerBuilder auth)
      throws Exception {

    auth
      .inMemoryAuthentication()
        .withUser("buzz") // Create a user with username "buzz"
          .password("infinity") // Password is "infinity"
          .authorities("ROLE_USER") // Assign the "ROLE_USER" authority
        .and()
        .withUser("woody") // Create another user "woody"
          .password("bullseye")
          .authorities("ROLE_USER");
  }
*/

//
// JDBC Authentication Example (uncomment if using JDBC authentication with DataSource)
//
/*
  @Autowired
  DataSource dataSource;

  @Override
  protected void configure(AuthenticationManagerBuilder auth)
      throws Exception {

    auth
      .jdbcAuthentication()
        .dataSource(dataSource); // Use JDBC authentication with the injected DataSource
  }
*/

/*
  Custom queries for JDBC authentication to specify where to find user credentials and authorities
*/
/*
  @Override
  protected void configure(AuthenticationManagerBuilder auth)
      throws Exception {

    auth
      .jdbcAuthentication()
        .dataSource(dataSource)
        .usersByUsernameQuery(
            "select username, password, enabled from Users where username=?")
        .authoritiesByUsernameQuery(
            "select username, authority from UserAuthorities where username=?")
        .passwordEncoder(new BCryptPasswordEncoder()); // Use BCrypt for password encoding
  }
*/

//
// LDAP Authentication Examples (uncomment if using LDAP authentication)
//
/*
  @Override
  protected void configure(AuthenticationManagerBuilder auth)
      throws Exception {
    auth
      .ldapAuthentication()
        .userSearchFilter("(uid={0})") // Filter to locate the user in LDAP
        .groupSearchFilter("member={0}"); // Filter to locate the group in LDAP
  }
*/

/*
  Example with custom search base for users and groups in LDAP
*/
/*
  @Override
  protected void configure(AuthenticationManagerBuilder auth)
      throws Exception {
    auth
      .ldapAuthentication()
        .userSearchBase("ou=people")
        .userSearchFilter("(uid={0})")
        .groupSearchBase("ou=groups")
        .groupSearchFilter("member={0}")
        .passwordCompare() // Compare password in LDAP
        .passwordEncoder(new BCryptPasswordEncoder()) // Use BCrypt for LDAP password encoding
        .passwordAttribute("passcode"); // LDAP attribute to compare password
  }
*/

/*
  Full LDAP configuration with context source
*/
/*
@Override
protected void configure(AuthenticationManagerBuilder auth)
    throws Exception {
  auth
    .ldapAuthentication()
      .userSearchBase("ou=people")
      .userSearchFilter("(uid={0})")
      .groupSearchBase("ou=groups")
      .groupSearchFilter("member={0}")
      .passwordCompare()
      .passwordEncoder(new BCryptPasswordEncoder())
      .passwordAttribute("passcode")
      .and()
      .contextSource()
        .url("ldap://tacocloud.com:389/dc=tacocloud,dc=com");
}
*/

}
