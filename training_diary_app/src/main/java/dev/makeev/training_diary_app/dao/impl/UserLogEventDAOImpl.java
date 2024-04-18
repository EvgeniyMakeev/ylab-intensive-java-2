package dev.makeev.training_diary_app.dao.impl;

import dev.makeev.training_diary_app.dao.UserLogEventDAO;
import dev.makeev.training_diary_app.exceptions.DaoException;
import dev.makeev.training_diary_app.exceptions.EmptyException;
import dev.makeev.training_diary_app.model.UserLogEvent;
import dev.makeev.training_diary_app.utils.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserLogEventDAOImpl implements UserLogEventDAO {

    private final static String ADD_SQL =
            "INSERT INTO non_public.user_log_events (user_login, message) VALUES (?,?)";
    private final static String GET_ALL_SQL = "SELECT * FROM non_public.user_log_events";
    private final static String GET_BY_LOGIN_SQL = GET_ALL_SQL + " WHERE user_login=?";

    private final ConnectionManager connectionManager;

    public UserLogEventDAOImpl(ConnectionManager connectionManager) {
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
    public List<UserLogEvent> getAllEvents() throws EmptyException {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(GET_ALL_SQL)) {
            return getUserLogEvents(statement);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<UserLogEvent> getAllEventsForUser(String login) throws EmptyException {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(GET_BY_LOGIN_SQL)) {
            statement.setString(1, login);
            return getUserLogEvents(statement);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private List<UserLogEvent> getUserLogEvents(PreparedStatement statement) throws SQLException, EmptyException {
        var result = statement.executeQuery();
        List<UserLogEvent> listOfUserLogEvents = new ArrayList<>();
        while (result.next()) {
            listOfUserLogEvents.add(
                    new UserLogEvent(result.getDate("date").toLocalDate(),
                            result.getString("user_login"),
                            result.getString("message"))
            );
        }
        if (listOfUserLogEvents.isEmpty()) {
            throw new EmptyException();
        }
        return listOfUserLogEvents;
    }
}
