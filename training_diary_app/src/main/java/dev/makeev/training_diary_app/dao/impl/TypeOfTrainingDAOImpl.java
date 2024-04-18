package dev.makeev.training_diary_app.dao.impl;

import dev.makeev.training_diary_app.dao.TypeOfTrainingDAO;
import dev.makeev.training_diary_app.exceptions.DaoException;
import dev.makeev.training_diary_app.model.TypeOfTraining;
import dev.makeev.training_diary_app.utils.ConnectionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TypeOfTrainingDAOImpl implements TypeOfTrainingDAO {

    private final static String ADD_SQL =
            "INSERT INTO non_public.types_of_training (type) VALUES (?)";
    private final static String GET_ALL_SQL = "SELECT * FROM non_public.types_of_training";
    private final static String GET_BY_ID_SQL = GET_ALL_SQL + " WHERE id=?";
    private static final String GET_BY_TYPE_SQL = GET_ALL_SQL + " WHERE type=?";
    private final ConnectionManager connectionManager;

    public TypeOfTrainingDAOImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }


    @Override
    public void add(String type) {
        try (var connection = connectionManager.open();
             var statementAdd = connection.prepareStatement(ADD_SQL)) {
            statementAdd.setString(1, type);
            statementAdd.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<TypeOfTraining> getAll() {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(GET_ALL_SQL)) {
            List<TypeOfTraining> listOfTraining = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next()) {
                listOfTraining.add(
                        new TypeOfTraining(result.getLong("id"),
                                result.getString("type"))
                );
            }
            return listOfTraining;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<TypeOfTraining> getById(long id) {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(GET_BY_ID_SQL)) {
            statement.setLong(1, id);
            TypeOfTraining typeOfTraining = null;
            var result = statement.executeQuery();
            if (result.next()) {
                typeOfTraining = new TypeOfTraining(id,result.getString("type"));
            }
            return Optional.ofNullable(typeOfTraining);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<TypeOfTraining> getByType(String type) {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(GET_BY_TYPE_SQL)) {
            statement.setString(1, type);
            TypeOfTraining typeOfTraining = null;
            var result = statement.executeQuery();
            if (result.next()) {
                long id = result.getLong("id");
                typeOfTraining = new TypeOfTraining(id,type);
            }
            return Optional.ofNullable(typeOfTraining);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
