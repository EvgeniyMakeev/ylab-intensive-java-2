package dev.makeev.training_diary_app.dao.impl;

import dev.makeev.training_diary_app.dao.TrainingOfUserDAO;
import dev.makeev.training_diary_app.exceptions.DaoException;
import dev.makeev.training_diary_app.exceptions.EmptyException;
import dev.makeev.training_diary_app.model.Training;
import dev.makeev.training_diary_app.utils.ConnectionManager;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainingOfUserDAOImpl implements TrainingOfUserDAO {
    private final static String ADD_SQL =
            "INSERT INTO non_public.trainings " +
                    "(user_login, type_of_training_id, date, duration, calories_burned) " +
                    "VALUES (?,?,?,?,?)";
    private final static String GET_ALL_SQL =
            "SELECT * FROM non_public.trainings";
    private final static String GET_ALL_TRAININGS_FOR_USER_SQL =
            GET_ALL_SQL + " WHERE user_login=? ORDER BY date";

    private static final String UPDATE_SQL = "UPDATE non_public.trainings " +
            "SET type_of_training_id=?, date=?, duration=?, calories_burned=?" +
            " WHERE training_id=?";
    private static final String DELETE_SQL =
            "DELETE FROM non_public.trainings WHERE id=?";
    private static final String GET_ALL_TRAININGS_FOR_USER_BY_TYPE_OF_TRAINING_SQL =
            GET_ALL_SQL + " WHERE user_login=? AND type_of_training_id=? ORDER BY date";

    private static final String ADD_ADDITIONAL_INFORMATION_FOR_TRAINING_SQL =
            "INSERT INTO non_public.additional_information " +
            "(training_id, information, value) " +
            "VALUES (?,?,?)";
    private static final String GET_ADDITIONAL_INFORMATION_FOR_TRAINING_SQL =
            "SELECT * FROM non_public.additional_information WHERE training_id=?";
    private static final String DELETE_ADDITIONAL_INFORMATION_SQL =
            "DELETE FROM non_public.additional_information WHERE training_id=?";


    private final ConnectionManager connectionManager;

    public TrainingOfUserDAOImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }


    @Override
    public void add(String login, long typeOfTrainingId, LocalDate date,
                    double duration, double caloriesBurned) {
        try (var connection = connectionManager.open();
             var statementAdd = connection.prepareStatement(ADD_SQL)) {

            statementAdd.setString(1, login);
            statementAdd.setLong(2, typeOfTrainingId);
            statementAdd.setDate(3, Date.valueOf(date));
            statementAdd.setDouble(4, duration);
            statementAdd.setDouble(5, caloriesBurned);

            statementAdd.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Training> getByLogin(String login) throws EmptyException {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(GET_ALL_TRAININGS_FOR_USER_SQL)) {
            statement.setString(1, login);
            var result = statement.executeQuery();
            List<Training> listOfTrainingsOfUser = new ArrayList<>();
            while (result.next()) {
                long id = result.getLong("id");
                long typeOfTrainingId = result.getLong("type_of_training_id");
                LocalDate date = result.getDate("date").toLocalDate();
                Double duration = result.getDouble("duration");
                Double caloriesBurned = result.getDouble("calories_burned");
                Training training = new Training(id, typeOfTrainingId, date, duration, caloriesBurned);
                listOfTrainingsOfUser.add(training);
            }
            if (listOfTrainingsOfUser.isEmpty()) {
                throw new EmptyException();
            }
            return listOfTrainingsOfUser;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Training> getAllTrainingsForUserByTypeOfTraining(String login, long typeOfTrainingId) {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(GET_ALL_TRAININGS_FOR_USER_BY_TYPE_OF_TRAINING_SQL)) {
            statement.setString(1, login);
            statement.setLong(2, typeOfTrainingId);
            var result = statement.executeQuery();
            List<Training> listOfTrainingsOfUser = new ArrayList<>();
            while (result.next()) {
                long trainingId = result.getLong("id");
                LocalDate date = result.getDate("date").toLocalDate();
                Double duration = result.getDouble("duration");
                Double caloriesBurned = result.getDouble("calories_burned");
                Training training = new Training(trainingId, typeOfTrainingId, date, duration, caloriesBurned);
                listOfTrainingsOfUser.add(training);
            }
            return listOfTrainingsOfUser;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void addAdditionalInformation(long trainingId, Map<String, Double> additionalInformation) {
        for (Map.Entry<String, Double> info : additionalInformation.entrySet()) {
            try (var connection = connectionManager.open();
                 var statement = connection.prepareStatement(ADD_ADDITIONAL_INFORMATION_FOR_TRAINING_SQL)) {
                statement.setLong(1, trainingId);
                statement.setString(2, info.getKey());
                statement.setDouble(3, info.getValue());

                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        }
    }
    @Override
    public Map<String, Double> getAdditionalInformation(long id) {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(GET_ADDITIONAL_INFORMATION_FOR_TRAINING_SQL)) {
            statement.setLong(1, id);
            var result = statement.executeQuery();
            Map<String, Double> additionalInformation = new HashMap<>();
            while (result.next()) {
                String information = result.getString("information");
                Double value = result.getDouble("value");
                additionalInformation.put(information, value);
            }
            return additionalInformation;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    @Override
    public void edit(long idOfTrainingForEdite, Training newTraining) {
        try (var connection = connectionManager.open();
             var statementDelete = connection.prepareStatement(UPDATE_SQL)) {
            statementDelete.setLong(1, newTraining.typeOfTrainingId());
            statementDelete.setDate(2, Date.valueOf(newTraining.date()));
            statementDelete.setDouble(3, newTraining.duration());
            statementDelete.setDouble(4, newTraining.caloriesBurned());

            statementDelete.setLong(5, idOfTrainingForEdite);

            statementDelete.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(long id) {
        try (var connection = connectionManager.open();
             var statementDelete =
                     connection.prepareStatement(DELETE_SQL);
             var statementDeleteAdditionalInformation =
                     connection.prepareStatement(DELETE_ADDITIONAL_INFORMATION_SQL)) {
            connection.setAutoCommit(false);

            statementDelete.setLong(1, id);
            statementDelete.executeUpdate();

            statementDeleteAdditionalInformation.setLong(1, id);
            statementDeleteAdditionalInformation.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
