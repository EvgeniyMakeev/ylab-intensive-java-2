package dev.makeev.training_diary_app.dao;

import dev.makeev.training_diary_app.model.Training;
import dev.makeev.training_diary_app.model.TypeOfTraining;

import java.time.LocalDate;
import java.util.List;

public interface TrainingOfUserDAO {
    void add(String login, TypeOfTraining type, LocalDate localDate,
             double duration, double caloriesBurned);

    List<Training> getByLogin(String login);

    List<Training> getAll();

    void edit(int index, String login, TypeOfTraining type, LocalDate localDate,
              double duration, double caloriesBurned);

    void delete(int index, String login);
}
