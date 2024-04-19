package dev.makeev.training_diary_app.dao.impl;

import dev.makeev.training_diary_app.dao.LogEventDAO;
import dev.makeev.training_diary_app.exceptions.DaoException;
import dev.makeev.training_diary_app.exceptions.EmptyException;
import dev.makeev.training_diary_app.model.LogEvent;
import dev.makeev.training_diary_app.utils.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code LogEventDAOImpl} class implements the {@link LogEventDAO} interface.
 * It provides methods to interact with the database to manage LogEvent entities.
 */
public class LogEventDAOImpl implements LogEventDAO {

    private final static String ADD_SQL =
            "INSERT INTO non_public.log_events (user_login, message) VALUES (?,?)";
    private final static String GET_ALL_SQL = "SELECT * FROM non_public.log_events";
    private final static String GET_BY_LOGIN_SQL = GET_ALL_SQL + " WHERE user_login=?";

    private final ConnectionManager connectionManager;

    /**
     * Constructs a new LogEventDAOImpl with the specified {@link ConnectionManager}.
     *
     * @param connectionManager The ConnectionManager used to manage database connections.
     */
    public LogEventDAOImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }


    @Override
    public void addEvent(String login, String message) {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(ADD_SQL)) {
            statement.setString(1, login);
            statement.setString(2, message);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<LogEvent> getAllEvents() throws EmptyException {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(GET_ALL_SQL)) {
            return getLogEvents(statement);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<LogEvent> getAllEventsForUser(String login) throws EmptyException {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(GET_BY_LOGIN_SQL)) {
            statement.setString(1, login);
            return getLogEvents(statement);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<LogEvent> getLogEvents(PreparedStatement statement) throws SQLException, EmptyException {
        var result = statement.executeQuery();
        List<LogEvent> listOfLogEvents = new ArrayList<>();
        while (result.next()) {
            listOfLogEvents.add(
                    new LogEvent(result.getDate("date").toLocalDate(),
                            result.getString("user_login"),
                            result.getString("message"))
            );
        }
        if (listOfLogEvents.isEmpty()) {
            throw new EmptyException();
        }
        return listOfLogEvents;
    }
}
