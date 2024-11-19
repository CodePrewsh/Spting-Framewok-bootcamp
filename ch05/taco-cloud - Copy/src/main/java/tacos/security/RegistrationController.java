package tacos.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.data.UserRepository;

@Controller
@RequestMapping("/register")
public class RegistrationController {

  // UserRepository to interact with the user data storage
  private UserRepository userRepo;

  // PasswordEncoder for encoding the password before saving
  private PasswordEncoder passwordEncoder;

  // Constructor to inject dependencies: UserRepository and PasswordEncoder
  public RegistrationController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
    this.userRepo = userRepo;
    this.passwordEncoder = passwordEncoder;
  }

  // Handler method for GET requests to "/register" to display the registration form
  @GetMapping
  public String registerForm() {
    return "registration"; // Returns the view name for the registration form
  }

  // Handler method for POST requests to "/register" to process registration form submission
  @PostMapping
  public String processRegistration(RegistrationForm form) {
    // Converts RegistrationForm to a User object and saves it to the repository with an encoded password
    userRepo.save(form.toUser(passwordEncoder));
    // Redirects to the login page after successful registration
    return "redirect:/login";
  }
}
