package tacos.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.Data;
import tacos.User;

/**
 * A form for registering a new user, with fields for user details and a method
 * to convert the form data into a User object.
 */
@Data  // Lombok annotation to generate getters, setters, equals, hashCode, and toString methods
public class RegistrationForm {

  private String username;  // Username chosen by the user
  private String password;  // Password chosen by the user
  private String fullname;  // Full name of the user
  private String street;    // Street address
  private String city;      // City address
  private String state;     // State address
  private String zip;       // ZIP code
  private String phone;     // Phone number

  /**
   * Converts this registration form into a User object with an encoded password.
   * @param passwordEncoder the encoder used to encrypt the password
   * @return a new User object populated with the form's data
   */
  public User toUser(PasswordEncoder passwordEncoder) {
    return new User(
            username,
            passwordEncoder.encode(password),  // Encode the password for security
            fullname, street, city, state, zip, phone);  // Set other user details
  }

}
