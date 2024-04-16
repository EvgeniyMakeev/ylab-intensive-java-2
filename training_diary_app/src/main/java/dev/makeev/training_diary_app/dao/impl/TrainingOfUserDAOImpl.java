package dev.makeev.training_diary_app.dao.impl;

import dev.makeev.training_diary_app.dao.TrainingOfUserDAO;
import dev.makeev.training_diary_app.model.Training;
import dev.makeev.training_diary_app.model.TypeOfTraining;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainingOfUserDAOImpl implements TrainingOfUserDAO {
    private final Map<String, List<Training>> mapOfTrainings;

    {
        mapOfTrainings = new HashMap<>();
    }

    @Override
    public void add(String login, TypeOfTraining type, LocalDate localDate,
                    double duration, double caloriesBurned) {
        Training training = new Training(type, localDate, duration, caloriesBurned);

        if (mapOfTrainings.containsKey(login)) {
            mapOfTrainings.get(login).add(training);
        } else {
            List<Training> list = new ArrayList<>();
            list.add(training);
            mapOfTrainings.put(login, list);
        }
    }

    @Override
    public List<Training> getByLogin(String login) {
        if (mapOfTrainings.containsKey(login)) {
            return mapOfTrainings.get(login);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Training> getAll() {
        List<Training> listOfAllTrainings = new ArrayList<>();
        for (List<Training> list : mapOfTrainings.values()) {
            listOfAllTrainings.addAll(list);
        }

        return listOfAllTrainings;
    }

    @Override
    public void edit(int index, String login, TypeOfTraining type, LocalDate localDate,
                     double duration, double caloriesBurned) {
        Training oldTraining = mapOfTrainings.get(login).get(index);
        Training editedTraining =
                new Training(type, localDate, duration, caloriesBurned, oldTraining.additionalInfo());

        mapOfTrainings.get(login).set(index, editedTraining);
    }

    @Override
    public void delete(int index, String login) {
        mapOfTrainings.get(login).remove(index);
    }
}
