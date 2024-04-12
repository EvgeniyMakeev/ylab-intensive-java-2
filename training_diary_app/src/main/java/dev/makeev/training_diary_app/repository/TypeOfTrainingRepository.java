package dev.makeev.training_diary_app.repository;

import dev.makeev.training_diary_app.model.TypeOfTraining;

import java.util.List;
import java.util.Optional;

public interface TypeOfTrainingRepository {
    void add(TypeOfTraining typeOfTraining);

    Optional<TypeOfTraining> getBy(String typeOfTraining);

    List<TypeOfTraining> getAll();
}
