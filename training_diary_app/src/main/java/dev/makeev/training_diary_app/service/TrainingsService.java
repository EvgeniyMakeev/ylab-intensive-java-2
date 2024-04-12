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

/**
 * {@code TrainingsService} is a service class that provides methods to interact
 * with training-related functionalities like adding, editing, and retrieving trainings.
 */
public class TrainingsService {

    private final TrainingOfUserDAO trainingOfUserDAO;
    private final TypeOfTrainingDAO typeOfTrainingDAO;

    /**
     * Constructs a new {@code TrainingsService} with the specified {@link TrainingOfUserDAO}
     * and {@link TypeOfTrainingDAO}.
     *
     * @param trainingOfUserDAO the DAO for training of user entities.
     * @param typeOfTrainingDAO the DAO for type of training entities.
     */
    public TrainingsService(TrainingOfUserDAO trainingOfUserDAO, TypeOfTrainingDAO typeOfTrainingDAO) {
        this.trainingOfUserDAO = trainingOfUserDAO;
        this.typeOfTrainingDAO = typeOfTrainingDAO;
    }

    /**
     * Retrieves all types of training.
     *
     * @return a list of all types of training.
     */
    public List<TypeOfTraining> getAllTypesOfTraining() {
        return typeOfTrainingDAO.getAll();
    }

    /**
     * Retrieves the type of training by its index.
     *
     * @param index the index of the type of training to retrieve.
     * @return the type of training with the specified index.
     * @throws EmptyException if no type of training is found.
     */
    public String getTypeOfTrainingByIndex(int index) throws EmptyException {
        return typeOfTrainingDAO.getByIndex(index).orElseThrow(EmptyException::new).type();
    }

    /**
     * Adds a new type of training.
     *
     * @param typeOfTraining the type of training to add.
     */
    public void addTypeOfTraining(String typeOfTraining) {
        typeOfTrainingDAO.add(typeOfTraining);
    }

    /**
     * Adds a new training for a user.
     *
     * @param login the login of the user.
     * @param typeOfTraining the type of training.
     * @param date the date of the training.
     * @param duration the duration of the training.
     * @param caloriesBurned the calories burned during the training.
     * @throws EmptyException if no type of training is found.
     * @throws UserNotFoundException if the user is not found.
     * @throws TrainingOnDateAlreadyExistsException if a training already exists on the given date.
     */
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

    /**
     * Retrieves all trainings for a user.
     *
     * @param login the login of the user.
     * @return a list of all trainings for the user.
     */
    public List<Training> getAllTrainingsForUser(String login) {
        List<Training> trainingList = trainingOfUserDAO.getByLogin(login);
        trainingList.sort(Comparator.comparing(Training::date));
        return trainingList;
    }

    /**
     * Retrieves all trainings.
     *
     * @return a list of all trainings.
     */
    public List<Training> getAll() {
        List<Training> trainingList = trainingOfUserDAO.getAll();
        trainingList.sort(Comparator.comparing(Training::date));
        return trainingList;
    }

    /**
     * Edits a training.
     *
     * @param index the index of the training to edit.
     * @param login the login of the user.
     * @param typeOfTraining the type of training.
     * @param date the date of the training.
     * @param duration the duration of the training.
     * @param caloriesBurned the calories burned during the training.
     * @throws TrainingOnDateAlreadyExistsException if a training already exists on the given date.
     * @throws EmptyException if no type of training is found.
     */
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

    /**
     * Deletes a training.
     *
     * @param index the index of the training to delete.
     * @param login the login of the user.
     */
    public void delete(int index, String login) {
        trainingOfUserDAO.delete(index - 1, login);
    }

    /**
     * Edits additional information of a training.
     *
     * @param training the training to edit.
     * @param info the additional information.
     * @param value the value to set.
     */
    public void editAdditionalInfo(Training training, String info, Double value) {
        training.additionalInfo().put(info, value);
    }

    /**
     * Retrieves all trainings for a user by the type of training.
     *
     * @param login the login of the user.
     * @param typeOfTraining the type of training.
     * @return a list of all trainings for the user by the type of training.
     */
    public List<Training> getAllTrainingsForUserByTypeOfTraining(String login, String typeOfTraining) {
        return trainingOfUserDAO.getByLogin(login).stream()
                .filter(training -> training.type().type().equals(typeOfTraining))
                .toList();
    }

    /**
     * Calculates the statistics for a list of trainings based on the user opinion.
     *
     * @param trainingList the list of trainings.
     * @param userOpinion the user's opinion.
     * @return the calculated statistics.
     */
    public Statistic getStatistic(List<Training> trainingList, int userOpinion) {
        List<Training> sortedList = trainingList.stream()
                .sorted(Comparator.comparing(Training::date))
                .toList();
        LocalDate from = sortedList.get(0).date();
        LocalDate to = sortedList.get(sortedList.size() - 1).date();

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
