package dev.makeev.training_diary_app.repository;

import dev.makeev.training_diary_app.model.Training;

import java.util.List;
import java.util.Map;

public interface TrainingOfUserRepository {
    void add(String login, Training training);

    List<Training> getBy(String login);

    Map<String, List<Training>> getAll();

    void delete(int index, String login);

    void edit(int index, String login, Training training);
}
