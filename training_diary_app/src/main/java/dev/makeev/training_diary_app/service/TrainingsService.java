package dev.makeev.training_diary_app.service;


import dev.makeev.training_diary_app.dao.TrainingOfUserDAO;
import dev.makeev.training_diary_app.dao.TypeOfTrainingDAO;
import dev.makeev.training_diary_app.exceptions.EmptyException;
import dev.makeev.training_diary_app.exceptions.TrainingOnDateAlreadyExistsException;
import dev.makeev.training_diary_app.exceptions.UserNotFoundException;
import dev.makeev.training_diary_app.model.Training;
import dev.makeev.training_diary_app.model.TypeOfTraining;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class TrainingsService {

    private final TrainingOfUserDAO trainingOfUserDAO = new TrainingOfUserDAO();

    private final TypeOfTrainingDAO typeOfTrainingDAO = new TypeOfTrainingDAO();


    public List<TypeOfTraining> getAllTypesOfTraining() {
        return typeOfTrainingDAO.getAll();
    }

    public int getSizeOfListOfTypes() {
        return typeOfTrainingDAO.getSizeOfList();
    }

    public String getTypeOfTrainingByIndex(int index) throws EmptyException {
        return typeOfTrainingDAO.getByIndex(index).orElseThrow(EmptyException::new).type();
    }

    public void addTypeOfTraining(String typeOfTraining) {
        typeOfTrainingDAO.add(typeOfTraining);
    }

    public void addTrainingOfUser(
            String login, String typeOfTraining, LocalDate date, double duration, double caloriesBurned)
            throws EmptyException, UserNotFoundException, TrainingOnDateAlreadyExistsException {

        if (trainingOfUserDAO.getByLogin(login).stream().anyMatch(training -> training.date().isEqual(date))) {
            throw new TrainingOnDateAlreadyExistsException();
        } else {
            TypeOfTraining type = typeOfTrainingDAO.getBy(typeOfTraining);
            trainingOfUserDAO.add(login, type, date, duration, caloriesBurned);
        }
    }

    public List<Training> getAllTrainingsForUser(String login) {
        List<Training> trainingList = trainingOfUserDAO.getByLogin(login);
        trainingList.sort(Comparator.comparing(Training::date));
        return trainingList;
    }

    public List<Training> getAll() {
        List<Training> trainingList = trainingOfUserDAO.getAll();
        trainingList.sort(Comparator.comparing(Training::date));
        return trainingList;
    }

    public void edite(int index, String login, String typeOfTraining, LocalDate date, double duration, double caloriesBurned)
            throws TrainingOnDateAlreadyExistsException, EmptyException {

        List<Training> trainingList = trainingOfUserDAO.getByLogin(login);

        for (int i = 0; i < trainingList.size(); i++) {
            if (i != index - 1 && trainingList.get(i).date().isEqual(date)) {
                throw new TrainingOnDateAlreadyExistsException();
            }
        }

        TypeOfTraining type = typeOfTrainingDAO.getBy(typeOfTraining);
        trainingOfUserDAO.edit(index - 1, login, type, date, duration,caloriesBurned);
    }

    public void delete(int index, String login) {
        trainingOfUserDAO.delete(index - 1, login);
    }
}