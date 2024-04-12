package dev.makeev.training_diary_app.service;

import dev.makeev.training_diary_app.dao.TrainingOfUserDAO;
import dev.makeev.training_diary_app.dao.TypeOfTrainingDAO;
import dev.makeev.training_diary_app.exceptions.EmptyException;
import dev.makeev.training_diary_app.exceptions.TrainingOnDateAlreadyExistsException;
import dev.makeev.training_diary_app.exceptions.UserNotFoundException;
import dev.makeev.training_diary_app.model.Statistic;
import dev.makeev.training_diary_app.model.Training;
import dev.makeev.training_diary_app.model.TypeOfTraining;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("TrainingsService Test")
@ExtendWith(MockitoExtension.class)
class TrainingsServiceTest {

    private static final String LOGIN = "TestUser";
    private static final String TYPE_1 = "TestType1";
    private static final LocalDate DATE_1 = LocalDate.now();
    private static final LocalDate DATE_2 = LocalDate.now().plusDays(1);
    private static final TypeOfTraining TYPE_OF_TRAINING_1 = mock(TypeOfTraining.class);
    private static final TypeOfTraining TYPE_OF_TRAINING_2 = mock(TypeOfTraining.class);
    private static final Training TRAINING = mock(Training.class);

    @Mock
    private TrainingOfUserDAO trainingOfUserDAO;

    @Mock
    private TypeOfTrainingDAO typeOfTrainingDAO;

    @InjectMocks
    private TrainingsService trainingsService;

    @Test
    @DisplayName("Get All Types Of Training - Should return list of all types of training")
    void getAllTypesOfTraining_shouldReturnListOfAllTypesOfTraining() {
        List<TypeOfTraining> mockTypes = List.of(TYPE_OF_TRAINING_1, TYPE_OF_TRAINING_2);
        when(typeOfTrainingDAO.getAll()).thenReturn(mockTypes);

        List<TypeOfTraining> result = trainingsService.getAllTypesOfTraining();

        assertThat(result).isEqualTo(mockTypes);
        Mockito.verify(typeOfTrainingDAO, Mockito.times(1)).getAll();
    }

    @Test
    @DisplayName("Get Type Of Training By Index - Should return type of training by index")
    void getTypeOfTrainingByIndex_shouldReturnTypeOfTrainingByIndex() throws EmptyException {
        when(typeOfTrainingDAO.getByIndex(0)).thenReturn(Optional.of(TYPE_OF_TRAINING_1));
        when(TYPE_OF_TRAINING_1.type()).thenReturn(TYPE_1);

        String result = trainingsService.getTypeOfTrainingByIndex(0);

        assertEquals(TYPE_1, result);
        Mockito.verify(typeOfTrainingDAO, Mockito.times(1)).getByIndex(0);
    }

    @Test
    @DisplayName("Add Type Of Training - Should call DAO add method with correct parameter")
    void addTypeOfTraining_shouldCallDAOAddMethodWithCorrectParameter() {
        trainingsService.addTypeOfTraining(TYPE_1);

        Mockito.verify(typeOfTrainingDAO, Mockito.times(1)).add(TYPE_1);
    }

    @Test
    @DisplayName("Add Training Of User - Should add training for user")
    void addTrainingOfUser_shouldAddTrainingForUser() throws EmptyException, UserNotFoundException, TrainingOnDateAlreadyExistsException {
        when(typeOfTrainingDAO.getBy(TYPE_1)).thenReturn(TYPE_OF_TRAINING_1);
        when(trainingOfUserDAO.getByLogin(LOGIN)).thenReturn(List.of());

        trainingsService.addTrainingOfUser(LOGIN, TYPE_1, DATE_1, 111.1, 222.2);

        Mockito.verify(trainingOfUserDAO, Mockito.times(1))
                .add(LOGIN, TYPE_OF_TRAINING_1, DATE_1, 111.1, 222.2);
    }

    @Test
    @DisplayName("Add Training Of User - Should throw TrainingOnDateAlreadyExistsException if training on date already exists")
    void addTrainingOfUser_shouldThrowTrainingOnDateAlreadyExistsExceptionIfTrainingOnDateAlreadyExists() {
        when(trainingOfUserDAO.getByLogin(LOGIN)).
                thenReturn(List.of(TRAINING));
        when(TRAINING.type()).thenReturn(TYPE_OF_TRAINING_1);
        when(TRAINING.date()).thenReturn(DATE_1);
        when(TRAINING.duration()).thenReturn(111.1);
        when(TRAINING.caloriesBurned()).thenReturn(222.2);

        assertThrows(TrainingOnDateAlreadyExistsException.class, () ->
                trainingsService.addTrainingOfUser(LOGIN, TYPE_1, DATE_1, 111.1, 222.2));
    }

    @Test
    @DisplayName("Get All Trainings For User - Should return all trainings for user")
    void getAllTrainingsForUser_shouldReturnAllTrainingsForUser() {
        List<Training> expectedTrainings = new ArrayList<>();
        when(trainingOfUserDAO.getByLogin(LOGIN)).thenReturn(expectedTrainings);

        List<Training> result = trainingsService.getAllTrainingsForUser(LOGIN);

        assertEquals(expectedTrainings, result);
    }

    @Test
    @DisplayName("Get All - Should return all trainings")
    void getAll_shouldReturnAllTrainings() {
        List<Training> expectedTrainings = new ArrayList<>();
        when(trainingOfUserDAO.getAll()).thenReturn(expectedTrainings);

        List<Training> result = trainingsService.getAll();

        assertEquals(expectedTrainings, result);
    }

    @Test
    @DisplayName("Edit - Should edit training")
    void edite_shouldEditTraining() {
        assertDoesNotThrow(() -> trainingsService.edite(1, LOGIN, TYPE_1, DATE_1, 111.1, 222.2));
    }

    @Test
    @DisplayName("Delete - Should delete training")
    void delete_shouldDeleteTraining() {
        assertDoesNotThrow(() -> trainingsService.delete(1, LOGIN));
    }

    @Test
    @DisplayName("Edit Additional Info - Should edit additional info of training")
    void editAdditionalInfo_shouldEditAdditionalInfoOfTraining() {
        Training training = new Training(new TypeOfTraining(TYPE_1), DATE_1, 111.1, 222.2);
        String info = "info";
        Double value = 100.0;

        trainingsService.editAdditionalInfo(training, info, value);

        assertTrue(training.additionalInfo().containsKey(info));
        assertEquals(value, training.additionalInfo().get(info));
    }

    @Test
    @DisplayName("Get All Trainings For User By Type Of Training - Should return trainings for user by type")
    void getAllTrainingsForUserByTypeOfTraining_shouldReturnTrainingsForUserByType() {
        List<Training> expectedTrainings = new ArrayList<>();
        when(trainingOfUserDAO.getByLogin(LOGIN)).thenReturn(expectedTrainings);

        List<Training> result = trainingsService.getAllTrainingsForUserByTypeOfTraining(LOGIN, TYPE_1);

        assertEquals(expectedTrainings, result);
    }

    @Test
    @DisplayName("Get Statistic - Should calculate statistics for trainings")
    void getStatistic_shouldCalculateStatisticsForTrainings() {
        List<Training> trainingList = new ArrayList<>();
        trainingList.add(new Training(new TypeOfTraining(TYPE_1), DATE_1, 40.0, 200.0));
        trainingList.add(new Training(new TypeOfTraining(TYPE_1), DATE_2, 60.0, 300.0));

        Statistic statistic = trainingsService.getStatistic(trainingList, 1);

        assertNotNull(statistic);
        assertEquals(40.0, statistic.minValue());
        assertEquals(60.0, statistic.maxValue());
        assertEquals(100.0, statistic.totalValue());
    }
}
