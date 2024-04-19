package dev.makeev.training_diary_app.dao;

import dev.makeev.training_diary_app.exceptions.EmptyException;
import dev.makeev.training_diary_app.model.TypeOfTraining;

import java.util.List;
import java.util.Optional;

/**
 * The {@code TypeOfTrainingDAO} interface provides methods for managing the persistence
 * of TypeOfTraining entities. It allows retrieving, adding, and querying TypeOfTraining
 * entities by their type or index.
 */
public interface TypeOfTrainingDAO {

    /**
     * Adds a new TypeOfTraining entity.
     *
     * @param type The type of the new TypeOfTraining entity to add.
     */
    void add(String type);

    /**
     * Retrieves a TypeOfTraining entity by its index.
     *
     * @param id The index of the TypeOfTraining entity to retrieve.
     * @return An {@code Optional} containing the TypeOfTraining entity if found,
     *         or empty if not found.
     * @throws EmptyException If no TypeOfTraining entity with the specified index is found.
     */
    Optional<TypeOfTraining> getById(long id) throws EmptyException;

    /**
     * Retrieves a list of all TypeOfTraining entities.
     *
     * @return The list of all TypeOfTraining entities.
     */
    List<TypeOfTraining> getAll();

    /**
     * Retrieves a TypeOfTraining entity by its type.
     *
     * @param type The type of the TypeOfTraining entity to retrieve.
     * @return An {@code Optional} containing the TypeOfTraining entity if found,
     *         or empty if not found.
     */
    Optional<TypeOfTraining> getByType(String type);
}
