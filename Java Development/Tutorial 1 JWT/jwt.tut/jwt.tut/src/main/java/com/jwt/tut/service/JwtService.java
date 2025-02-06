package com.jwt.tut.service;

import com.jwt.tut.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * Service class for handling JSON Web Tokens (JWT).
 * Provides methods to generate, validate, and extract information from JWTs.
 */
@Service
public class JwtService {

    // Secret key used for signing and verifying JWTs. It should be kept secure.
    private final String SECRET_KEY = "d6f0fabda25f0faaaefbe8a1251dd4a39440f3e4ac5316811c9182a0fb5f8196";

    /**
     * Extracts the username (subject) from the provided token.
     *
     * @param token the JWT from which to extract the username.
     * @return the username embedded in the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Validates if the token is associated with the given user and checks for expiration.
     *
     * @param token the JWT to validate.
     * @param user  the user details to match against the token.
     * @return true if the token is valid, false otherwise.
     */
    public boolean isValid(String token, UserDetails user) {
        String username = extractUsername(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Checks if the token has expired.
     *
     * @param token the JWT to check.
     * @return true if the token has expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the token.
     *
     * @param token the JWT from which to extract the expiration date.
     * @return the expiration date of the token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the token using the provided resolver function.
     *
     * @param token    the JWT to extract the claim from.
     * @param resolver a function to resolve the specific claim.
     * @param <T>      the type of the claim.
     * @return the resolved claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    /**
     * Extracts all claims from the token.
     *
     * @param token the JWT to parse.
     * @return the claims embedded in the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Generates a new JWT for the provided user.
     *
     * @param user the user for whom the token is generated.
     * @return the generated JWT as a string.
     */
    public String generateToken(User user) {
        String token = Jwts
                .builder()
                .setSubject(user.getUsername()) // Sets the subject as the username
                .setIssuedAt(new Date(System.currentTimeMillis())) // Sets the current time as the issued date
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // Sets the expiration to 24 hours from now
                .signWith(getSigninKey()) // Signs the token using the secret key
                .compact();
        return token;
    }

    /**
     * Retrieves the signing key used for generating and validating JWTs.
     *
     * @return the secret key used for signing JWTs.
     */
    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
