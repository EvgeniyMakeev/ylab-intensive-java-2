package dev.makeev.training_diary_app.dao;

import dev.makeev.training_diary_app.exceptions.EmptyException;
import dev.makeev.training_diary_app.model.UserLogEvent;

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
     * @param login   The login associated with the log event.
     * @param message The message or description of the log event.
     */
    void addEvent(String login, String message);

    /**
     * Retrieves a list of all UserLogEvent entities.
     *
     * @return The list of all UserLogEvent entities.
     */
    List<UserLogEvent> getAllEvents() throws EmptyException;

    List<UserLogEvent> getAllEventsForUser(String login) throws EmptyException;
}
