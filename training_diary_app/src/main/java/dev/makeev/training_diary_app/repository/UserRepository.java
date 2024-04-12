package dev.makeev.training_diary_app.repository;

import dev.makeev.training_diary_app.model.User;

import java.util.List;

public interface UserRepository {
    void add(User user);

    User getBy(String login);

    List<User> getAll();
}
