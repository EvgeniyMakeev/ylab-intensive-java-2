package dev.makeev.training_diary_app.dao;

import dev.makeev.training_diary_app.exceptions.EmptyException;
import dev.makeev.training_diary_app.model.Training;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * The {@code TrainingOfUserDAO} interface provides methods for managing the persistence
 * of Training entities associated with specific users. It allows adding, retrieving,
 * editing, and deleting training records, as well as fetching all training records
 * for a particular user.
 */
public interface TrainingOfUserDAO {

    /**
     * Adds a new training record for a user.
     *
     * @param login             The login of the user to add the training record to.
     * @param typeOfTrainingId  The type of the training.
     * @param date              The date of the training.
     * @param duration          The duration of the training in minutes.
     * @param caloriesBurned    The number of calories burned during the training.
     */
    void add(String login, long typeOfTrainingId, LocalDate date,
             double duration, double caloriesBurned);

    /**
     * Retrieves all training records associated with a specific user.
     *
     * @param login The login of the user to retrieve the training records for.
     * @return A list of all training records associated with the user.
     */
    List<Training> getByLogin(String login) throws EmptyException;

    /**
     * Retrieves all training records from the DAO.
     *
     * @return A list of all training records.
     */
    Map<String, List<Training>> getAll() throws EmptyException;

    void edit(long idOfTrainingForEdite, Training newTraining);

    void delete(long id);

    List<Training> getAllTrainingsForUserByTypeOfTraining(String login, long typeOfTrainingId) throws EmptyException;

    void addAdditionalInformation(long trainingId, Map<String, Double> additionalInformation);

    Map<String, Double> getAdditionalInformation(long id) throws EmptyException;
}
