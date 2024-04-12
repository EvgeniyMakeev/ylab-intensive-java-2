package dev.makeev.training_diary_app.repository.impl;

import dev.makeev.training_diary_app.model.Training;
import dev.makeev.training_diary_app.repository.TrainingOfUserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainingOfUserRepositoryImpl implements TrainingOfUserRepository {

    private final Map<String, List<Training>> mapOfTrainings = new HashMap<>();

    @Override
    public void add(String login, Training training) {
        if (mapOfTrainings.containsKey(login)) {
            mapOfTrainings.get(login).add(training);
        } else {
            List<Training> list = new ArrayList<>();
            list.add(training);
            mapOfTrainings.put(login, list);
        }
    }

    @Override
    public List<Training> getBy(String login) {
        if (mapOfTrainings.containsKey(login)) {
            return mapOfTrainings.get(login);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public Map<String, List<Training>> getAll() {
        return mapOfTrainings;
    }


    @Override
    public void delete(int index, String login) {
        mapOfTrainings.get(login).remove(index);
    }

    @Override
    public void edit(int index, String login, Training training) {
        mapOfTrainings.get(login).set(index, training);
    }
}
