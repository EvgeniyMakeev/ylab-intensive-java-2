package dev.makeev.training_diary_app.repository;

import dev.makeev.training_diary_app.model.TypeOfTraining;

import java.util.List;
import java.util.Optional;

/**
 * {@code TypeOfTrainingRepository} is an interface that defines the contract for managing
 * the persistence of {@link TypeOfTraining} entities.
 *
 * <p>It provides methods to add, retrieve, and list types of training.
 */
public interface TypeOfTrainingRepository {

    /**
     * Adds a new {@link TypeOfTraining} to the repository.
     *
     * @param typeOfTraining the type of training to add.
     */
    void add(TypeOfTraining typeOfTraining);

    /**
     * Retrieves a specific {@link TypeOfTraining} by its name.
     *
     * @param typeOfTraining the name of the type of training to retrieve.
     * @return an {@code Optional} containing the type of training if found, or empty if not found.
     */
    Optional<TypeOfTraining> getBy(String typeOfTraining);

    /**
     * Retrieves a list of all available {@link TypeOfTraining} entities.
     *
     * @return the list of all available types of training.
     */
    List<TypeOfTraining> getAll();
}
