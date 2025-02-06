package com.jwt.tut.repository;

import com.jwt.tut.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 * Extends {@link JpaRepository} to provide CRUD operations and additional methods for database interaction.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by their username.
     * This method is used in authentication to retrieve user details from the database.
     *
     * @param username the username of the user to be retrieved.
     * @return an {@link Optional} containing the user if found, or empty if no user exists with the given username.
     */
    Optional<User> findByUsername(String username);
}
