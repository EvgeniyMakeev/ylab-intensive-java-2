package dev.makeev.training_diary_app.dao;

import dev.makeev.training_diary_app.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    void add(String login, String password);

    Optional<User> getBy(String login);

    List<User> getAll();
}
