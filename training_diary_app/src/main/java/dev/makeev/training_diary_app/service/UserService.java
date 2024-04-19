package dev.makeev.training_diary_app.service;

import dev.makeev.training_diary_app.dao.UserDAO;
import dev.makeev.training_diary_app.exceptions.UserNotFoundException;
import dev.makeev.training_diary_app.exceptions.VerificationException;
import dev.makeev.training_diary_app.model.User;

import java.util.List;
import java.util.Optional;

/**
 * The {@code UserService} class provides methods to manage user-related operations.
 * It encapsulates the business logic related to user management, such as adding users,
 * verifying credentials, and checking user roles.
 */
public class UserService {

    /**
     * The UserDAO instance for managing user data.
     */
    private final UserDAO userDAO;

    /**
     * Constructs a {@code UserService} with the specified UserDAO instance.
     *
     * @param userDAO The UserDAO instance to use for data access.
     */
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Adds a new user with the specified login and password.
     *
     * @param login    The login of the user.
     * @param password The password of the user.
     */
    public void addUser(String login, String password) {
        userDAO.add(login, password);
    }

    /**
     * Retrieves a list of all registered users.
     *
     * @return A list of all User entities.
     */
    public List<User> getAll() {
        return userDAO.getAll();
    }

    /**
     * Checks if a user with the specified login exists.
     *
     * @param login The login to check.
     * @return {@code true} if the user exists, {@code false} otherwise.
     */
    public boolean existByLogin(String login) {
        return userDAO.getByLogin(login).isPresent();
    }

    /**
     * Verifies the credentials of a user.
     *
     * @param login    The login to verify.
     * @param password The password to verify.
     * @throws VerificationException If the verification fails.
     */
    public void checkCredentials(String login, String password) throws VerificationException {
        Optional<User> user = userDAO.getByLogin(login);
        if (user.isEmpty() || !user.get().password().equals(password)) {
            throw new VerificationException();
        }
    }

    /**
     * Checks if a user is an admin.
     *
     * @param login The login of the user.
     * @return {@code true} if the user is an admin, {@code false} otherwise.
     * @throws UserNotFoundException If the user is not found.
     */
    public boolean isAdmin(String login) throws UserNotFoundException {
        return userDAO.getByLogin(login).orElseThrow(UserNotFoundException::new).admin();
    }
}
