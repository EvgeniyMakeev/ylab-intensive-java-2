package dev.makeev.training_diary_app.dao.impl;

import dev.makeev.training_diary_app.dao.TrainingOfUserDAO;
import dev.makeev.training_diary_app.model.Training;
import dev.makeev.training_diary_app.model.TypeOfTraining;
import dev.makeev.training_diary_app.repository.TrainingOfUserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TrainingOfUserDAOImpl implements TrainingOfUserDAO {

    private final TrainingOfUserRepository repository;

    public TrainingOfUserDAOImpl(TrainingOfUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void add(String login, TypeOfTraining type, LocalDate localDate,
                    double duration, double caloriesBurned) {
        Training training = new Training(type, localDate, duration, caloriesBurned);
        repository.add(login, training);
    }

    @Override
    public List<Training> getByLogin(String login) {
        return repository.getBy(login);
    }

    @Override
    public List<Training> getAll() {
        List<Training> listOfAllTrainings = new ArrayList<>();
        for (List<Training> list : repository.getAll().values()) {
            listOfAllTrainings.addAll(list);
        }
        return listOfAllTrainings;
    }

    @Override
    public void edit(int index, String login, TypeOfTraining type, LocalDate localDate,
                     double duration, double caloriesBurned) {
        Training oldTraining = repository.getAll().get(login).get(index);
        Training editedTraining = new Training(type, localDate, duration, caloriesBurned, oldTraining.additionalInfo());
        repository.edit(index, login, editedTraining);
    }
    @Override
    public void delete(int index, String login) {
        repository.delete(index, login);
    }
}
