package dev.makeev.training_diary_app.dao;

import dev.makeev.training_diary_app.exceptions.EmptyException;
import dev.makeev.training_diary_app.model.TypeOfTraining;

import java.util.List;
import java.util.Optional;

public interface TypeOfTrainingDAO {
    TypeOfTraining getBy(String type) throws EmptyException;

    List<TypeOfTraining> getAll();

    void add(String type);

    Optional<TypeOfTraining> getByIndex(int index) throws EmptyException;
}
