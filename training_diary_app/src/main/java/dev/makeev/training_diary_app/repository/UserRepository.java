package dev.makeev.training_diary_app.repository;

import dev.makeev.training_diary_app.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository {

    /**
     * A map storing User entities with login as the key.
     */
    private final Map<String, User> mapOfUser;

    {
        mapOfUser = new HashMap<>();
        String login = "admin";
        User admin = new User(login, "admin", true);
        mapOfUser.put(login, admin);
    }

    public void add(User user) {
        mapOfUser.put(user.login(), user);
    }

    public User getBy(String login) {
        return mapOfUser.get(login);
    }

    public List<User> getAll() {
        return mapOfUser.values().stream().toList();
    }
}
