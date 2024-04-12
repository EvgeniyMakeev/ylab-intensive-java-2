package dev.makeev.training_diary_app.repository;

import dev.makeev.training_diary_app.model.User;

import java.util.List;

/**
 * {@code UserRepository} is an interface that defines the contract for managing
 * the persistence of {@link User} entities.
 *
 * <p>It provides methods to add, retrieve, and list user entities.
 */
public interface UserRepository {

    /**
     * Adds a new {@link User} to the repository.
     *
     * @param user the user to add.
     */
    void add(User user);

    /**
     * Retrieves a specific {@link User} by its login.
     *
     * @param login the login of the user to retrieve.
     * @return the user with the specified login.
     */
    User getBy(String login);

    /**
     * Retrieves a list of all available {@link User} entities.
     *
     * @return the list of all available users.
     */
    List<User> getAll();
}
