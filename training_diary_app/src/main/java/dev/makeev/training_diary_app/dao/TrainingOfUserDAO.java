package dev.makeev.training_diary_app.dao;

import dev.makeev.training_diary_app.model.Training;
import dev.makeev.training_diary_app.model.TypeOfTraining;

import java.time.LocalDate;
import java.util.List;

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
     * @param login          The login of the user to add the training record to.
     * @param type           The type of the training.
     * @param localDate      The date of the training.
     * @param duration       The duration of the training in minutes.
     * @param caloriesBurned The number of calories burned during the training.
     */
    void add(String login, TypeOfTraining type, LocalDate localDate,
             double duration, double caloriesBurned);

    /**
     * Retrieves all training records associated with a specific user.
     *
     * @param login The login of the user to retrieve the training records for.
     * @return A list of all training records associated with the user.
     */
    List<Training> getByLogin(String login);

    /**
     * Retrieves all training records from the DAO.
     *
     * @return A list of all training records.
     */
    List<Training> getAll();

    /**
     * Edits an existing training record.
     *
     * @param index          The index of the training record to edit.
     * @param login          The login of the user associated with the training record.
     * @param type           The new type of the training.
     * @param localDate      The new date of the training.
     * @param duration       The new duration of the training in minutes.
     * @param caloriesBurned The new number of calories burned during the training.
     */
    void edit(int index, String login, TypeOfTraining type, LocalDate localDate,
              double duration, double caloriesBurned);

    /**
     * Deletes a training record.
     *
     * @param index The index of the training record to delete.
     * @param login The login of the user associated with the training record.
     */
    void delete(int index, String login);
}
