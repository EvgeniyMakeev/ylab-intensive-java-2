package dev.makeev.training_diary_app.dao.impl;

import dev.makeev.training_diary_app.dao.UserDAO;
import dev.makeev.training_diary_app.model.User;
import dev.makeev.training_diary_app.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * The {@code UserDAO} class is responsible for managing the persistence of User entities.
 * It provides methods to retrieve, add, and query User information, as well as handle
 * user-related operations such as login verification and indication submission.
 */
public class UserDAOImpl implements UserDAO {

    private final UserRepository repository;

    public UserDAOImpl(UserRepository repository) {
        this.repository = repository;
        repository.add(new User("DemoUser", "1234",false));
    }

    @Override
    public void add(String login, String password) {
        repository.add(new User(login, password, false));
    }

    /**
     * Retrieves a User entity by its login.
     *
     * @param login The login of the User to retrieve.
     * @return An {@code Optional} containing the User if found, or empty if not found.
     */
    @Override
    public Optional<User> getBy(String login){
        return Optional.ofNullable(repository.getBy(login));
    }

    /**
     * Retrieves a list of all User entities.
     *
     * @return The list of all User entities.
     */
    @Override
    public List<User> getAll() {
        return repository.getAll();
    }

}
