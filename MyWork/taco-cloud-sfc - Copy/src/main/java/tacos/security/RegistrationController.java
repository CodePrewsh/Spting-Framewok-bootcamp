package tacos.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.data.UserRepository;

/**
 * Controller to handle user registration requests.
 */
@Controller
@RequestMapping("/register")  // Maps this controller to handle requests at "/register"
public class RegistrationController {

  private UserRepository userRepo;  // Repository to manage user data persistence
  private PasswordEncoder passwordEncoder;  // Encoder for encrypting user passwords

  /**
   * Constructor injection of UserRepository and PasswordEncoder dependencies.
   * @param userRepo the user repository for saving user data
   * @param passwordEncoder the encoder for securing passwords
   */
  public RegistrationController(
          UserRepository userRepo, PasswordEncoder passwordEncoder) {
    this.userRepo = userRepo;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Displays the registration form.
   * @return the name of the registration form view
   */
  @GetMapping
  public String registerForm() {
    return "registration";
  }

  /**
   * Processes the registration form submission.
   * Converts the RegistrationForm to a User object, saves it in the repository,
   * and redirects to the login page.
   * @param form the registration form data
   * @return redirection to the login page
   */
  @PostMapping
  public String processRegistration(RegistrationForm form) {
    userRepo.save(form.toUser(passwordEncoder));  // Save the encoded user to the database
    return "redirect:/login";  // Redirects to login after successful registration
  }

}
