package dev.makeev.training_diary_app.dao.impl;

import dev.makeev.training_diary_app.dao.UserDAO;
import dev.makeev.training_diary_app.exceptions.DaoException;
import dev.makeev.training_diary_app.model.User;
import dev.makeev.training_diary_app.utils.ConnectionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The {@code UserDAOImpl} class implements the {@link UserDAO} interface.
 * It provides methods to interact with the database to manage User entities.
 */
public class UserDAOImpl implements UserDAO {

    private final ConnectionManager connectionManager;

    public UserDAOImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void add(String login, String password) {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(DAOConstants.ADD_USER_SQL)) {
            statement.setString(1, login);
            statement.setString(2, password);
            statement.setBoolean(3, false);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<User> getByLogin(String login) {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(DAOConstants.GET_USER_BY_LOGIN_SQL)) {
            statement.setString(1, login);
            User user = null;
            var result = statement.executeQuery();
            if (result.next()) {
                user = new User(login,
                        result.getString("password"),
                        result.getBoolean("admin"));
            }
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<User> getAll() {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(DAOConstants.GET_ALL_USERS_SQL)) {
            List<User> listOfUsers = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next()) {
                listOfUsers.add(
                        new User(result.getString("login"),
                                result.getString("password"),
                                result.getBoolean("admin"))
                );
            }
            return listOfUsers;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
