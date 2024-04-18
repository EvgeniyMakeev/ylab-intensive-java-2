package dev.makeev.training_diary_app.dao;

import dev.makeev.training_diary_app.exceptions.EmptyException;
import dev.makeev.training_diary_app.model.LogEvent;

import java.util.List;

/**
 * The {@code UserLogEventDAO} interface provides methods for managing
 * the persistence of UserLogEvent entities. It allows adding new log events
 * and retrieving a list of all log events.
 */
public interface LogEventDAO {

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
    List<LogEvent> getAllEvents() throws EmptyException;

    List<LogEvent> getAllEventsForUser(String login) throws EmptyException;
}
