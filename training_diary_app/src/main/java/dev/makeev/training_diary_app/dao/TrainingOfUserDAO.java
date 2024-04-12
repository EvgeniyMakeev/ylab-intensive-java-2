package dev.makeev.training_diary_app.dao;

import dev.makeev.training_diary_app.model.Training;
import dev.makeev.training_diary_app.model.TypeOfTraining;
import dev.makeev.training_diary_app.repository.TrainingOfUserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TrainingOfUserDAO {

    private final TrainingOfUserRepository repository = new TrainingOfUserRepository();


    public void add(String login, TypeOfTraining type, LocalDate localDate,
                    double duration, double caloriesBurned) {
        Training training = new Training(type, localDate, duration, caloriesBurned);
        repository.add(login, training);
    }

    public List<Training> getByLogin(String login) {
        return repository.getBy(login);
    }


    public List<Training> getAll() {
        List<Training> listOfAllTrainings = new ArrayList<>();
        for (List<Training> list : repository.getAll().values()) {
            listOfAllTrainings.addAll(list);
        }
        return listOfAllTrainings;
    }

    public void edit(int index, String login, TypeOfTraining type, LocalDate localDate,
                     double duration, double caloriesBurned) {
        Training oldTraining = repository.getAll().get(login).get(index);
        Training editedTraining = new Training(type, localDate, duration, caloriesBurned, oldTraining.additionalInfo());
        repository.edit(index, login, editedTraining);
    }

    public void delete(int index, String login) {
        repository.delete(index, login);
    }
}
