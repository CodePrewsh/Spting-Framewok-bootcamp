package com.jwt.tut.model;

/**
 * A model class representing the response sent back to the client after a successful authentication.
 * This contains the JWT token that the client will use for subsequent requests.
 */
public class AuthResponse {

    // The JWT token issued to the client
    private String token;

    /**
     * Constructor to initialize the AuthResponse with a given token.
     *
     * @param token the JWT token
     */
    public AuthResponse(String token) {
        this.token = token;
    }

    /**
     * Getter method for retrieving the token.
     *
     * @return the JWT token
     */
    public String getToken() {
        return token;
    }

    /**
     * Setter method for updating the token.
     *
     * @param token the JWT token
     */
    public void setToken(String token) {
        this.token = token;
    }
}
