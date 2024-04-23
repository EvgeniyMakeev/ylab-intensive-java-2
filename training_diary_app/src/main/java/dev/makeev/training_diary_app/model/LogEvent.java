package dev.makeev.training_diary_app.model;

import java.time.LocalDate;

/**
 * A record representing an event related to a user, including the date, the user involved, and a message.
 */
public record LogEvent(
        LocalDate date,
        String login,
        String message) {
}
