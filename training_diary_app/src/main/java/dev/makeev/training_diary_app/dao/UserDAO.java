package dev.makeev.training_diary_app.dao;

import dev.makeev.training_diary_app.model.User;

import java.util.List;
import java.util.Optional;

/**
 * The {@code UserDAO} interface provides methods for managing the persistence
 * of User entities. It allows adding, retrieving, and querying User entities by their login.
 */
public interface UserDAO {

    /**
     * Adds a new User entity.
     *
     * @param login    The login of the new User entity to add.
     * @param password The password of the new User entity to add.
     */
    void add(String login, String password);

    /**
     * Retrieves a User entity by its login.
     *
     * @param login The login of the User entity to retrieve.
     * @return An {@code Optional} containing the User entity if found,
     *         or empty if not found.
     */
    Optional<User> getBy(String login);

    /**
     * Retrieves a list of all User entities.
     *
     * @return The list of all User entities.
     */
    List<User> getAll();
}
