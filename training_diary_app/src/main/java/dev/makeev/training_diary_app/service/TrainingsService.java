package dev.makeev.training_diary_app.service;

import dev.makeev.training_diary_app.dao.TrainingOfUserDAO;
import dev.makeev.training_diary_app.dao.TypeOfTrainingDAO;
import dev.makeev.training_diary_app.exceptions.EmptyException;
import dev.makeev.training_diary_app.exceptions.TrainingOnDateAlreadyExistsException;
import dev.makeev.training_diary_app.exceptions.UserNotFoundException;
import dev.makeev.training_diary_app.model.Statistic;
import dev.makeev.training_diary_app.model.Training;
import dev.makeev.training_diary_app.model.TypeOfTraining;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class TrainingsService {

    private final TrainingOfUserDAO trainingOfUserDAO = new TrainingOfUserDAO();

    private final TypeOfTrainingDAO typeOfTrainingDAO = new TypeOfTrainingDAO();

    {
        try {
            addTrainingOfUser("Hooch", typeOfTrainingDAO.getAll().get(0),
                    LocalDate.of(2024,2,2), 175.0, 153.5);
            addTrainingOfUser("Hooch", typeOfTrainingDAO.getAll().get(0),
                    LocalDate.of(2024,1,2), 215.0, 60.5);
            addTrainingOfUser("Hooch", typeOfTrainingDAO.getAll().get(0),
                    LocalDate.of(2024,5,2), 105.0, 600.5);
            addTrainingOfUser("Hooch", typeOfTrainingDAO.getAll().get(0),
                    LocalDate.of(2024,8,2), 155.0, 550.5);
            addTrainingOfUser("Hooch", typeOfTrainingDAO.getAll().get(0),
                    LocalDate.of(2024,7,2), 20.0, 70.5);

            addTrainingOfUser("Hooch", typeOfTrainingDAO.getAll().get(2),
                    LocalDate.of(2024,10,2), 115.0, 1070.5);
            addTrainingOfUser("Hooch", typeOfTrainingDAO.getAll().get(2),
                    LocalDate.of(2024,11,2), 265.0, 65.5);
            addTrainingOfUser("Hooch", typeOfTrainingDAO.getAll().get(2),
                    LocalDate.of(2024,4,2), 175.1, 650.5);
            addTrainingOfUser("Hooch", typeOfTrainingDAO.getAll().get(2),
                    LocalDate.of(2024,3,2), 900.0, 50.5);
            addTrainingOfUser("Hooch", typeOfTrainingDAO.getAll().get(2),
                    LocalDate.of(2024,9,2), 22.0, 75.5);
        } catch (EmptyException | TrainingOnDateAlreadyExistsException | UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public List<TypeOfTraining> getAllTypesOfTraining() {
        return typeOfTrainingDAO.getAll();
    }

    public int getSizeOfListOfTypes() {
        return typeOfTrainingDAO.getSizeOfList();
    }

    public TypeOfTraining getTypeOfTrainingByIndex(int index) throws EmptyException {
        return typeOfTrainingDAO.getByIndex(index).orElseThrow(EmptyException::new);
    }

    public void addTypeOfTraining(String typeOfTraining) {
        typeOfTrainingDAO.add(typeOfTraining);
    }

    public void addTrainingOfUser(
            String login, TypeOfTraining type, LocalDate date, double duration, double caloriesBurned)
            throws EmptyException, UserNotFoundException, TrainingOnDateAlreadyExistsException {

        if (trainingOfUserDAO.getByLogin(login).stream().anyMatch(training -> training.date().isEqual(date))) {
            throw new TrainingOnDateAlreadyExistsException();
        } else {
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

    public void edite(int index, String login, TypeOfTraining typeOfTraining, LocalDate date, double duration, double caloriesBurned)
            throws TrainingOnDateAlreadyExistsException {

        List<Training> trainingList = trainingOfUserDAO.getByLogin(login);

        for (int i = 0; i < trainingList.size(); i++) {
            if (i != index - 1 && trainingList.get(i).date().isEqual(date)) {
                throw new TrainingOnDateAlreadyExistsException();
            }
        }
        trainingOfUserDAO.edit(index - 1, login, typeOfTraining, date, duration,caloriesBurned);
    }

    public void delete(int index, String login) {
        trainingOfUserDAO.delete(index - 1, login);
    }

    public List<Training> getAllTrainingsForUserByTypeOfTraining(String login, TypeOfTraining typeOfTraining) {
        return trainingOfUserDAO.getByLogin(login).stream()
                .filter(training -> training.type().equals(typeOfTraining))
                .toList();
    }

    public Statistic getStatistic(List<Training> trainingList, int userOpinion) {
        List<Training> sortedList = trainingList.stream()
                .sorted(Comparator.comparing(Training::date))
                .toList();
        LocalDate from = sortedList.get(0).date();
        LocalDate to = sortedList.get(trainingList.size() - 1).date();

        int numbersOfTraining = trainingList.size();

        Double maxValue = Double.MIN_VALUE;
        Double minValue = Double.MAX_VALUE;
        Double totalValue = 0.0;
        Double averageValue = totalValue / numbersOfTraining;

        for (Training training : trainingList) {
            double currentValue = 0.0;

            switch (userOpinion) {
                case 1 -> currentValue = training.duration();
                case 2 -> currentValue = training.caloriesBurned();
            }

            if(currentValue > maxValue) {
                maxValue = training.duration();
            }

            if(currentValue < minValue) {
                minValue = training.duration();
            }

            totalValue += training.duration();
        }

        return new Statistic(from, to, minValue, averageValue, maxValue, totalValue, trainingList);
    }
}
