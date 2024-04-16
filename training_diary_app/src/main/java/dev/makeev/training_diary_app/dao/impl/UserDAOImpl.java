package dev.makeev.training_diary_app.dao.impl;

import dev.makeev.training_diary_app.dao.UserDAO;
import dev.makeev.training_diary_app.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The {@code UserDAO} class is responsible for managing the persistence of User entities.
 * It provides methods to retrieve, add, and query User information, as well as handle
 * user-related operations such as login verification and indication submission.
 */
public class UserDAOImpl implements UserDAO {

    /**
     * A map storing User entities with login as the key.
     */
    private final Map<String, User> mapOfUser;

    {
        mapOfUser = new HashMap<>();
        String loginAdmin = "admin";
        User admin = new User(loginAdmin, "admin", true);
        mapOfUser.put(loginAdmin, admin);

        String loginDemoUser = "DemoUser";
        User demoUser = new User(loginDemoUser, "1234", false);
        mapOfUser.put(loginDemoUser, demoUser);
    }

    @Override
    public void add(String login, String password) {
        User user = new User(login, password, false);
        mapOfUser.put(user.login(), user);
    }

    /**
     * Retrieves a User entity by its login.
     *
     * @param login The login of the User to retrieve.
     * @return An {@code Optional} containing the User if found, or empty if not found.
     */
    @Override
    public Optional<User> getByLogin(String login) {
        return Optional.ofNullable(mapOfUser.get(login));
    }

    /**
     * Retrieves a list of all User entities.
     *
     * @return The list of all User entities.
     */
    @Override
    public List<User> getAll() {
        return mapOfUser.values().stream().toList();
    }
}
