package dev.makeev.training_diary_app.repository;

import dev.makeev.training_diary_app.model.Training;

import java.util.List;
import java.util.Map;

/**
 * {@code TrainingOfUserRepository} is an interface that defines the contract for managing
 * the persistence of {@link Training} entities associated with users.
 *
 * <p>It provides methods to add, retrieve, edit, and delete user-specific training sessions.
 */
public interface TrainingOfUserRepository {

    /**
     * Adds a new {@link Training} session for a specific user.
     *
     * @param login the login of the user.
     * @param training the training session to add.
     */
    void add(String login, Training training);

    /**
     * Retrieves a list of {@link Training} sessions for a specific user.
     *
     * @param login the login of the user.
     * @return the list of training sessions for the specified user.
     */
    List<Training> getBy(String login);

    /**
     * Retrieves a mapping of all {@link Training} sessions grouped by user login.
     *
     * @return a mapping of user logins to their respective training sessions.
     */
    Map<String, List<Training>> getAll();

    /**
     * Deletes a specific {@link Training} session for a user.
     *
     * @param index the index of the training session to delete.
     * @param login the login of the user.
     */
    void delete(int index, String login);

    /**
     * Edits a specific {@link Training} session for a user.
     *
     * @param index the index of the training session to edit.
     * @param login the login of the user.
     * @param training the updated training session details.
     */
    void edit(int index, String login, Training training);
}
