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
     * @throws EmptyException if no training records are found.
     */
    List<Training> getByLogin(String login) throws EmptyException;

    /**
     * Retrieves all training records of a specific type associated with a user.
     *
     * @param login           The login of the user to retrieve the training records for.
     * @param typeOfTrainingId The ID of the type of training to filter by.
     * @return A list of all training records of the specified type associated with the user.
     */
    List<Training> getAllTrainingsForUserByTypeOfTraining(String login, long typeOfTrainingId);

    /**
     * Adds additional information to a specific training record.
     *
     * @param trainingId            The ID of the training record to add information to.
     * @param additionalInformation A map containing additional information.
     */
    void addAdditionalInformation(long trainingId, Map<String, Double> additionalInformation);

    /**
     * Retrieves additional information for a specific training record.
     *
     * @param id The ID of the training record to retrieve additional information for.
     * @return A map containing the additional information for the training record.
     * @throws EmptyException if no additional information is found.
     */
    Map<String, Double> getAdditionalInformation(long id) throws EmptyException;

    /**
     * Edits an existing training record.
     *
     * @param idOfTrainingForEdite The ID of the training record to edit.
     * @param newTypeOfTrainingId  The new type of the training.
     * @param newDate              The new date of the training.
     * @param newDuration          The new duration of the training in minutes.
     * @param newCaloriesBurned    The new number of calories burned during the training.
     */
    void edit(long idOfTrainingForEdite,
              long newTypeOfTrainingId,
              LocalDate newDate,
              Double newDuration,
              Double newCaloriesBurned);

    /**
     * Deletes a training record.
     *
     * @param id The ID of the training record to delete.
     */
    void delete(long id);
}
