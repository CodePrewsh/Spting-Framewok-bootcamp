package com.jwt.tut.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Entity class representing a user in the system.
 * Implements {@link UserDetails} to integrate with Spring Security for authentication and authorization.
 */
@Setter
@Getter
@Entity
@Table(name = "users") // Maps this class to the "users" table in the database
public class User implements UserDetails {

    // Primary key for the User entity, with auto-incremented values
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // The user's first name, stored in the "first_name" column
    @Column(name = "first_name")
    private String firstName;

    // The user's last name, stored in the "last_name" column
    @Column(name = "last_name")
    private String lastName;

    // The username used for authentication, stored in the "username" column
    @Column(name = "username")
    private String username;

    // The hashed password for the user, stored in the "password" column
    @Column(name = "password")
    private String password;

    // Enum representing the user's role, stored as a string in the database
    @Enumerated(value = EnumType.STRING)
    private Role role;

    /**
     * Returns the authorities granted to the user, based on their role.
     *
     * @return a collection of granted authorities (e.g., "ADMIN", "USER").
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Converts the user's role to a granted authority
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Indicates whether the user's account has expired.
     *
     * @return true as accounts in this example are always non-expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is locked.
     *
     * @return true as accounts in this example are always non-locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) have expired.
     *
     * @return true as credentials in this example are always non-expired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled (active).
     *
     * @return true as users in this example are always enabled.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
