package tacos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tacos.User;
import tacos.data.UserRepository;

// Marks this class as a service that provides custom user authentication logic
@Service
public class UserRepositoryUserDetailsService implements UserDetailsService {

  // Repository to access user data from the database
  private UserRepository userRepo;

  // Constructor injection of the UserRepository
  @Autowired
  public UserRepositoryUserDetailsService(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  /**
   * Loads the user by username for authentication.
   *
   * @param username the username to load the user by
   * @return UserDetails object if the user is found
   * @throws UsernameNotFoundException if the user is not found
   */
  @Override
  public UserDetails loadUserByUsername(String username)
          throws UsernameNotFoundException {
    // Searches for the user by username in the repository
    User user = userRepo.findByUsername(username);

    // If user is found, return the UserDetails implementation (User in this case)
    if (user != null) {
      return user;
    }

    // Throws exception if the user is not found
    throw new UsernameNotFoundException("User '" + username + "' not found");
  }
}
