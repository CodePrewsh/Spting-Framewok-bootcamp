package tacos;

import java.util.Arrays;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * Represents a user in the system. Implements UserDetails to integrate with Spring Security.
 */
@Entity  // Marks this class as a JPA entity to be persisted in the database
@Data  // Lombok annotation to automatically generate getters, setters, toString, and other methods
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)  // Lombok constructor to allow private no-arg constructor
@RequiredArgsConstructor  // Lombok constructor that requires arguments for final fields
public class User implements UserDetails {

  private static final long serialVersionUID = 1L;  // Serial version UID for the entity

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)  // Automatically generate unique IDs for each user
  private Long id;

  private final String username;  // The user's chosen username
  private final String password;  // The user's password (encoded)
  private final String fullname;  // Full name of the user
  private final String street;    // Street address of the user
  private final String city;      // City address of the user
  private final String state;     // State address of the user
  private final String zip;       // ZIP code address of the user
  private final String phoneNumber;  // Phone number of the user

  /**
   * Returns the authorities (roles) assigned to the user.
   * In this case, the user is granted the "ROLE_USER" authority.
   * @return a collection of granted authorities (roles)
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));  // Assigns the "ROLE_USER" authority
  }

  /**
   * Returns whether the user's account is expired.
   * In this case, it is always active.
   * @return true if the account is not expired
   */
  @Override
  public boolean isAccountNonExpired() {
    return true;  // Account is not expired
  }

  /**
   * Returns whether the user's account is locked.
   * In this case, it is always active.
   * @return true if the account is not locked
   */
  @Override
  public boolean isAccountNonLocked() {
    return true;  // Account is not locked
  }

  /**
   * Returns whether the user's credentials (password) are expired.
   * In this case, it is always valid.
   * @return true if the credentials are not expired
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return true;  // Credentials (password) are not expired
  }

  /**
   * Returns whether the user's account is enabled.
   * In this case, it is always enabled.
   * @return true if the account is enabled
   */
  @Override
  public boolean isEnabled() {
    return true;  // Account is enabled
  }

}
