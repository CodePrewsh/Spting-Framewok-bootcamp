package tacos.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.data.UserRepository;

// Marks this class as a Spring MVC Controller
@Controller
// Maps requests with "/register" URL to this controller
@RequestMapping("/register")
public class RegistrationController {

  // Repository for user-related database operations
  private UserRepository userRepo;

  // Password encoder to encode passwords before saving to the database
  private PasswordEncoder passwordEncoder;

  // Constructor that injects the UserRepository and PasswordEncoder
  public RegistrationController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
    this.userRepo = userRepo;
    this.passwordEncoder = passwordEncoder;
  }

  // Handles GET requests to "/register", returning the "registration" view (registration form)
  @GetMapping
  public String registerForm() {
    return "registration";
  }

  // Handles POST requests to "/register", processes the registration form submission
  @PostMapping
  public String processRegistration(RegistrationForm form) {
    // Saves the user to the repository after converting and encoding the password
    userRepo.save(form.toUser(passwordEncoder));

    // Redirects the user to the login page after successful registration
    return "redirect:/login";
  }
}
