package tacos.security;

// Import statements for necessary Spring Security and application components
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tacos.User;
import tacos.data.UserRepository;

/**
 * Service class that implements the UserDetailsService interface,
 * enabling the application to load user-specific data from the database.
 * This class interacts with the UserRepository to retrieve user data by username.
 */
@Service
public class UserRepositoryUserDetailsService implements UserDetailsService {

  private UserRepository userRepo;

  /**
   * Constructor for dependency injection of UserRepository.
   *
   * @param userRepo The repository for accessing User data.
   */
  @Autowired
  public UserRepositoryUserDetailsService(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  /**
   * Loads the user's details by username. This method is required by
   * the UserDetailsService interface and is called during the
   * authentication process to retrieve user information.
   *
   * @param username The username of the user to be authenticated.
   * @return UserDetails object containing user information.
   * @throws UsernameNotFoundException if the username is not found.
   */
  @Override
  public UserDetails loadUserByUsername(String username)
          throws UsernameNotFoundException {
    // Retrieve the user by username from the repository
    User user = userRepo.findByUsername(username);

    // If the user is found, return the User object (which implements UserDetails)
    if (user != null) {
      return user;
    }

    // If the user is not found, throw UsernameNotFoundException
    throw new UsernameNotFoundException(
            "User '" + username + "' not found");
  }

}
