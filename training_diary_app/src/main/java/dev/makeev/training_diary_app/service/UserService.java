package dev.makeev.training_diary_app.service;


import dev.makeev.training_diary_app.dao.UserDAO;
import dev.makeev.training_diary_app.exceptions.UserNotFoundException;
import dev.makeev.training_diary_app.exceptions.VerificationException;
import dev.makeev.training_diary_app.model.User;

import java.util.List;
import java.util.Optional;

public class UserService {

    /**
     * The UserDAO instance for managing user data.
     */
    private final UserDAO userDAO = new UserDAO();

    /**
     * Adds a new user with the specified login and password.
     *
     * @param login    The login of the user.
     * @param password The password of the user.
     */
    public void addUser(String login, String password) {
        userDAO.add(login, password);
    }

    public boolean existByLogin(String login) {
        return userDAO.getBy(login).isPresent();
    }

    /**
     * Verifies the credentials of a user.
     *
     * @param login    The login to verify.
     * @param password The password to verify.
     * @throws VerificationException If the verification fails.
     */
    public void checkCredentials(String login, String password) throws VerificationException {
        Optional<User> user = userDAO.getBy(login);
        if (user.isEmpty() || !user.get().password().equals(password)) {
            throw new VerificationException();
        }
    }

    /**
     * Checks if a user is an admin.
     *
     * @param login The login of the user.
     * @return {@code true} if the user is an admin, {@code false} otherwise.
     */
    public boolean isAdmin(String login) throws UserNotFoundException {
        return userDAO.getBy(login).orElseThrow(UserNotFoundException::new).admin();
    }

    public List<User> getAll() {
        return userDAO.getAll();
    }

}
