package dev.makeev.training_diary_app.dao;

import dev.makeev.training_diary_app.model.UserLogEvent;

import java.time.LocalDate;
import java.util.List;

/**
 * The {@code UserLogEventDAO} interface provides methods for managing
 * the persistence of UserLogEvent entities. It allows adding new log events
 * and retrieving a list of all log events.
 */
public interface UserLogEventDAO {

    /**
     * Adds a new UserLogEvent.
     *
     * @param date    The date of the log event.
     * @param login   The login associated with the log event.
     * @param message The message or description of the log event.
     */
    void add(LocalDate date, String login, String message);

    /**
     * Retrieves a list of all UserLogEvent entities.
     *
     * @return The list of all UserLogEvent entities.
     */
    List<UserLogEvent> getAll();
}
