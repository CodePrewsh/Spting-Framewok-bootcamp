package com.jwt.tut.service;

import com.jwt.tut.model.AuthResponse;
import com.jwt.tut.model.User;
import com.jwt.tut.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class to handle authentication and registration logic.
 * Provides methods for registering new users and authenticating existing ones.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructor for {@link AuthService}.
     *
     * @param userRepository        the repository to manage user data.
     * @param passwordEncoder       the encoder for hashing passwords.
     * @param jwtService            the service to generate and validate JWT tokens.
     * @param authenticationManager the manager to handle user authentication.
     */
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registers a new user in the system.
     *
     * @param request the user details to be registered.
     * @return an {@link AuthResponse} containing the generated JWT token for the user.
     */
    public AuthResponse register(User request) {
        // Create a new user object and set its fields from the registration request
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        // Encode the password for security before saving it in the database
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        // Save the new user in the database
        user = userRepository.save(user);

        // Generate a JWT token for the newly registered user
        String token = jwtService.generateToken(user);

        // Return the token wrapped in an AuthResponse object
        return new AuthResponse(token);
    }

    /**
     * Authenticates an existing user and generates a JWT token upon successful login.
     *
     * @param request the user credentials (username and password) for authentication.
     * @return an {@link AuthResponse} containing the generated JWT token for the user.
     */
    public AuthResponse authentication(User request) {
        // Authenticate the user's credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Retrieve the authenticated user from the database
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        // Generate a JWT token for the authenticated user
        String token = jwtService.generateToken(user);

        // Return the token wrapped in an AuthResponse object
        return new AuthResponse(token);
    }
}
