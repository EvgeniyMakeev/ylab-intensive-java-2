package dev.makeev.training_diary_app.model;

/**
 * A record representing a user with login credentials and administrative rights.
 */
public record User(String login,
                   String password,
                   Boolean admin) {
}
