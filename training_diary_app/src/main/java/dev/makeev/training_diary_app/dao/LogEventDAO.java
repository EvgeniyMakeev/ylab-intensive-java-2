package dev.makeev.training_diary_app.dao;

import dev.makeev.training_diary_app.exceptions.EmptyException;
import dev.makeev.training_diary_app.model.LogEvent;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * The {@code LogEventDAO} interface provides methods for managing
 * the persistence of LogEvent entities. It allows adding new log events
 * and retrieving a list of all log events.
 */
public interface LogEventDAO {

    /**
     * Adds a new LogEvent.
     *
     * @param login   The login associated with the log event.
     * @param message The message or description of the log event.
     */
    void addEvent(String login, String message);

    /**
     * Retrieves a list of all UserLogEvent entities.
     *
     * @return The list of all UserLogEvent entities.
     * @throws EmptyException if no log events are found.
     */
    List<LogEvent> getAllEvents() throws EmptyException;

    /**
     * Retrieves a list of all UserLogEvent entities for a specific user.
     *
     * @param login The login associated with the log events.
     * @return The list of all UserLogEvent entities for the user.
     * @throws EmptyException if no log events are found for the user.
     */
    List<LogEvent> getAllEventsForUser(String login) throws EmptyException;

    /**
     * Retrieves a list of LogEvent entities from the result of a prepared statement.
     *
     * @param statement The prepared statement containing the executed SQL query.
     * @return The list of UserLogEvent entities.
     * @throws SQLException  if a database access error occurs.
     * @throws EmptyException if no log events are found.
     */
    List<LogEvent> getLogEvents(PreparedStatement statement) throws SQLException, EmptyException;
}
