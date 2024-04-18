package dev.makeev.training_diary_app.service;

import dev.makeev.training_diary_app.dao.TrainingOfUserDAO;
import dev.makeev.training_diary_app.dao.TypeOfTrainingDAO;
import dev.makeev.training_diary_app.exceptions.EmptyException;
import dev.makeev.training_diary_app.exceptions.TrainingOnDateAlreadyExistsException;
import dev.makeev.training_diary_app.exceptions.UserNotFoundException;
import dev.makeev.training_diary_app.model.Statistic;
import dev.makeev.training_diary_app.model.Training;
import dev.makeev.training_diary_app.model.TrainingOfUser;
import dev.makeev.training_diary_app.model.TypeOfTraining;

import java.time.LocalDate;
import java.util.*;

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
     * @param id the id of the type of training to retrieve.
     * @return the type of training with the specified index.
     * @throws EmptyException if no type of training is found.
     */
    public TypeOfTraining getTypeOfTrainingById(long id) throws EmptyException {
        return typeOfTrainingDAO.getById(id).orElseThrow(EmptyException::new);
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
     * @throws EmptyException if no type of training is found.
     * @throws UserNotFoundException if the user is not found.
     * @throws TrainingOnDateAlreadyExistsException if a training already exists on the given date.
     */
    public void addTrainingOfUser(String login, String typeOfTraining,
                                  LocalDate date, Double duration, Double caloriesBurned)
            throws EmptyException, UserNotFoundException, TrainingOnDateAlreadyExistsException {
        TypeOfTraining type = typeOfTrainingDAO.getByType(typeOfTraining).orElseThrow(EmptyException::new);
        if (trainingOfUserDAO.getByLogin(login).stream()
                .anyMatch(training -> training.date().isEqual(date))) {
            throw new TrainingOnDateAlreadyExistsException();
        } else {
            trainingOfUserDAO.add(login, type.id(), date, duration, caloriesBurned);
        }
    }

    /**
     * Retrieves all trainings for a user.
     *
     * @param login the login of the user.
     * @return a list of all trainings for the user.
     */
    public List<TrainingOfUser> getAllTrainingsForUser(String login) throws EmptyException {
        List<Training> trainingList = trainingOfUserDAO.getByLogin(login);
        trainingList.sort(Comparator.comparing(Training::date));
        return getTrainingOfUsers(login, trainingList);
    }

    /**
     * Retrieves all trainings for a user by the type of training.
     *
     * @param login the login of the user.
     * @param typeOfTrainingId the type of training id.
     * @return a list of all trainings for the user by the type of training.
     */
    public List<TrainingOfUser> getAllTrainingsForUserByTypeOfTraining(String login, long typeOfTrainingId) throws EmptyException {
        List<Training> trainingList = trainingOfUserDAO.getAllTrainingsForUserByTypeOfTraining(login, typeOfTrainingId);
        trainingList.sort(Comparator.comparing(Training::date));
        return getTrainingOfUsers(login, trainingList);
    }

    private List<TrainingOfUser> getTrainingOfUsers(String login, List<Training> trainingList) throws EmptyException {
        List<TrainingOfUser> trainingOfUserList = new ArrayList<>();
        for (Training training : trainingList) {
            TypeOfTraining typeOfTraining =
                    typeOfTrainingDAO.getById(training.typeOfTrainingId())
                            .orElseThrow(EmptyException::new);
            Map<String, Double> additionalInformation =
                    trainingOfUserDAO.getAdditionalInformation(training.id());
            trainingOfUserList.add(
                    new TrainingOfUser(
                            login, typeOfTraining.type(), training, additionalInformation));
        }
        return trainingOfUserList;
    }

    public void delete(long id) {
        trainingOfUserDAO.delete(id);
    }

    /**
     * Edits additional information of a training.
     *
     * @param info the additional information.
     * @param value the value to set.
     */
    public void editAdditionalInfo(long idOfTrainingForEdite, String info, Double value) throws EmptyException {
        Map<String, Double> additionalInformation = trainingOfUserDAO.getAdditionalInformation(idOfTrainingForEdite);
        additionalInformation.put(info, value);
        trainingOfUserDAO.addAdditionalInformation(idOfTrainingForEdite, additionalInformation);
    }

    /**
     * Edits a training.
     *
     * @throws TrainingOnDateAlreadyExistsException if a training already exists on the given date.
     * @throws EmptyException if no type of training is found.
     */
    public void edite(long idOfTrainingForEdite, TrainingOfUser newTrainingOfUser)
            throws TrainingOnDateAlreadyExistsException, EmptyException {
        String login = newTrainingOfUser.login();
        List<Training> trainingList = trainingOfUserDAO.getByLogin(login);
        LocalDate newDate = newTrainingOfUser.training().date();
        for (Training training : trainingList) {
            if (training.id() != idOfTrainingForEdite && training.date().isEqual(newDate)) {
                throw new TrainingOnDateAlreadyExistsException();
            }
        }
        trainingOfUserDAO.edit(
                idOfTrainingForEdite, newTrainingOfUser.training().typeOfTrainingId(),
                newTrainingOfUser.training().date(),
                newTrainingOfUser.training().duration(),
                newTrainingOfUser.training().caloriesBurned());
    }

    /**
     * Calculates the statistics for a list of trainings based on the user opinion.
     *
     * @param userOpinion the user's opinion.
     * @return the calculated statistics.
     */
    public Statistic getStatistic(List<TrainingOfUser> trainingOfUserList, int userOpinion) {
        List<Training> trainingList = new ArrayList<>();

        LocalDate from = trainingOfUserList.get(0).training().date();
        LocalDate to = trainingOfUserList.get(trainingOfUserList.size() - 1).training().date();

        int numbersOfTraining = trainingOfUserList.size();

        double maxValue = Double.MIN_VALUE;
        double minValue = Double.MAX_VALUE;
        double totalValue = 0.0;

        for (TrainingOfUser trainingOfUser : trainingOfUserList) {
            Training training = trainingOfUser.training();
            trainingList.add(training);
            double currentValue = 0.0;

            switch (userOpinion) {
                case 1 -> currentValue = training.duration();
                case 2 -> currentValue = training.caloriesBurned();
            }

            if(currentValue > maxValue) {
                maxValue = currentValue;
            }

            if(currentValue < minValue) {
                minValue = currentValue;
            }

            totalValue += currentValue;
        }
        Double averageValue = totalValue / numbersOfTraining;

        return new Statistic(from, to, minValue, averageValue, maxValue, totalValue, trainingList);
    }
}
