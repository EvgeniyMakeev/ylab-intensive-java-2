package dev.makeev.training_diary_app.repository;

import dev.makeev.training_diary_app.model.UserLogEvent;

import java.util.List;

/**
 * {@code LogsRepository} is an interface that defines the contract for managing
 * the persistence of {@link UserLogEvent} entities.
 *
 * <p>It provides methods to add a new user log event and retrieve all user log events.
 */
public interface LogsRepository {

    /**
     * Adds a new {@link UserLogEvent} to the repository.
     *
     * @param userLogEvent the user log event to add.
     */
    void add(UserLogEvent userLogEvent);

    /**
     * Retrieves a list of all {@link UserLogEvent} entities stored in the repository.
     *
     * @return the list of all user log events.
     */
    List<UserLogEvent> getAll();
}
