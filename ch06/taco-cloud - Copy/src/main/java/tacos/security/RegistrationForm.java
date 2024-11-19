package tacos.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.Data;
import tacos.User;

// Lombok annotation to generate getters, setters, and other utility methods
@Data
public class RegistrationForm {

  // Fields to store registration details from the form
  private String username;
  private String password;
  private String fullname;
  private String street;
  private String city;
  private String state;
  private String zip;
  private String phone;

  /**
   * Converts the RegistrationForm to a User entity.
   * Encodes the password using the provided PasswordEncoder.
   *
   * @param passwordEncoder the PasswordEncoder to encode the password
   * @return a new User instance with encoded password and form details
   */
  public User toUser(PasswordEncoder passwordEncoder) {
    return new User(
            username,                    // Sets the username
            passwordEncoder.encode(password),  // Encodes and sets the password
            fullname,                    // Sets the full name
            street,                      // Sets the street address
            city,                        // Sets the city
            state,                       // Sets the state
            zip,                         // Sets the ZIP code
            phone                        // Sets the phone number
    );
  }
}
