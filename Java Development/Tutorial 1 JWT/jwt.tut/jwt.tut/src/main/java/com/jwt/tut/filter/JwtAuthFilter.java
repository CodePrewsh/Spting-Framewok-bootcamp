package com.jwt.tut.filter;

// Importing required classes and annotations
import com.jwt.tut.service.JwtService;
import com.jwt.tut.service.UserDetailsServiceImp;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Mark this class as a Spring component to enable dependency injection
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    // Service to handle JWT-related operations
    private final JwtService jwtService;

    // Custom user details service to load user-specific data
    private final UserDetailsServiceImp userDetailsImp;

    // Constructor to initialize the JWT service and user details service
    public JwtAuthFilter(JwtService jwtService, UserDetailsServiceImp userDetailsImp) {
        this.jwtService = jwtService;
        this.userDetailsImp = userDetailsImp;
    }

    /**
     * Filters incoming requests to validate JWT tokens and set authentication in the security context.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if any servlet-related error occurs
     * @throws IOException if any I/O error occurs
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Retrieve the "Authorization" header from the HTTP request
        String authHeader = request.getHeader("Authorization");

        // Check if the header is null or does not start with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Pass the request to the next filter if the token is not present
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the JWT token from the Authorization header
        String token = authHeader.substring(7);

        // Extract the username from the token
        String username = jwtService.extractUsername(token);

        // Check if the username is not null and the user is not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load user details using the username
            UserDetails userDetails = userDetailsImp.loadUserByUsername(username);

            // Validate the token against the user details
            if (jwtService.isValid(token, userDetails)) {
                // Create an authentication token with user details and authorities
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                // Set additional details for the authentication token
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Set the authentication token in the SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Pass the request to the next filter in the chain
        filterChain.doFilter(request, response);
    }
}
