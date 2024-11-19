package tacos.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.Data;
import tacos.User;

@Data  // Lombok annotation to automatically generate getters, setters, toString, equals, and hashCode methods
public class RegistrationForm {

  // Fields to capture the user's registration details
  private String username;
  private String password;
  private String fullname;
  private String street;
  private String city;
  private String state;
  private String zip;
  private String phone;

  // Converts the RegistrationForm to a User object, with the password encoded
  public User toUser(PasswordEncoder passwordEncoder) {
    return new User(
            username, // The username field
            passwordEncoder.encode(password), // The password is encoded before storing
            fullname, // Full name of the user
            street, // Street address
            city, // City of the user
            state, // State of the user
            zip, // ZIP code
            phone); // Phone number of the user
  }
}
