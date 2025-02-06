package com.jwt.tut.service;

import com.jwt.tut.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of the UserDetailsService interface.
 * Responsible for retrieving user details for authentication and authorization.
 */
@Service
public class UserDetailsServiceImp implements UserDetailsService {
    // Repository for accessing user data from the database.
    private final UserRepository userRepository;

    /**
     * Constructor for injecting the UserRepository dependency.
     *
     * @param userRepository the repository for accessing user data.
     */
    public UserDetailsServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads the user details by their username.
     *
     * @param username the username to search for in the database.
     * @return the UserDetails object containing the user's information.
     * @throws UsernameNotFoundException if the user with the specified username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username) // Searches for the user in the repository by username.
                .orElseThrow(() -> new UsernameNotFoundException("User not found!")); // Throws exception if the user is not found.
    }
}
