package dev.makeev.training_diary_app.repository;

import dev.makeev.training_diary_app.model.Training;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainingOfUserRepository {

    private final Map<String, List<Training>> mapOfTrainings = new HashMap<>();

    public void add(String login, Training training) {
        if (mapOfTrainings.containsKey(login)) {
            mapOfTrainings.get(login).add(training);
        } else {
            List<Training> list = new ArrayList<>();
            list.add(training);
            mapOfTrainings.put(login, list);
        }
    }

    public List<Training> getBy(String login) {
        if (mapOfTrainings.containsKey(login)) {
            return mapOfTrainings.get(login);
        } else {
            return new ArrayList<>();
        }
    }

    public Map<String, List<Training>> getAll() {
        return mapOfTrainings;
    }


    public void delete(int index, String login) {
        mapOfTrainings.get(login).remove(index);
    }

    public void edit(int index, String login, Training training) {
        mapOfTrainings.get(login).set(index, training);
    }
}
