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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
    private static final Training TRAINING_1 = mock(Training.class);
    private static final Training TRAINING_2 = mock(Training.class);
    private static final TrainingOfUser TRAINING_OF_USER_1 = mock(TrainingOfUser.class);
    private static final TrainingOfUser TRAINING_OF_USER_2 = mock(TrainingOfUser.class);

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
        verify(typeOfTrainingDAO, times(1)).getAll();
    }

    @Test
    @DisplayName("Get Type Of Training By Id - Should return type of training by id")
    void getTypeOfTrainingById_shouldReturnTypeOfTrainingById() throws EmptyException {
        when(typeOfTrainingDAO.getById(1)).thenReturn(Optional.of(TYPE_OF_TRAINING_1));

        TypeOfTraining result = trainingsService.getTypeOfTrainingById(1);

        assertThat(result).isEqualTo(TYPE_OF_TRAINING_1);
        verify(typeOfTrainingDAO, times(1)).getById(1);
    }

    @Test
    @DisplayName("Get Type Of Training By Type - Should return type when it exists")
    public void testGetTypeOfTrainingByType_WhenTypeExists() {
        when(typeOfTrainingDAO.getByType(TYPE_1)).thenReturn(Optional.of(TYPE_OF_TRAINING_1));

        Optional<TypeOfTraining> result = trainingsService.getTypeOfTrainingByType(TYPE_1);

        assertThat(result).isPresent();
        assertThat(TYPE_OF_TRAINING_1).isEqualTo(result.get());
        verify(typeOfTrainingDAO).getByType(TYPE_1);
    }

    @Test
    @DisplayName("Get Type Of Training By Type - Should return empty optional when type does not exist")
    public void testGetTypeOfTrainingByType_WhenTypeDoesNotExist() {
        String type = "NotExistType";
        when(typeOfTrainingDAO.getByType(type)).thenReturn(Optional.empty());

        Optional<TypeOfTraining> result = trainingsService.getTypeOfTrainingByType(type);

        assertThat(result).isEmpty();
        verify(typeOfTrainingDAO).getByType(type);
    }

    @Test
    @DisplayName("Add Type Of Training - Should call DAO add method with correct parameter")
    void addTypeOfTraining_shouldCallDAOAddMethodWithCorrectParameter() {
        trainingsService.addTypeOfTraining(TYPE_1);

        verify(typeOfTrainingDAO, times(1)).add(TYPE_1);
    }

    @Test
    @DisplayName("Add Training Of User - Should add training for user")
    void addTrainingOfUser_shouldAddTrainingForUser() throws EmptyException, UserNotFoundException, TrainingOnDateAlreadyExistsException {
        when(typeOfTrainingDAO.getByType(TYPE_1)).thenReturn(Optional.ofNullable(TYPE_OF_TRAINING_1));
        when(typeOfTrainingDAO.getByType(TYPE_1).get().id()).thenReturn(1L);
        trainingsService.addTrainingOfUser(LOGIN, TYPE_1, DATE_1, 111.1, 222.2);

        verify(trainingOfUserDAO, times(1))
                .add(LOGIN, 1L, DATE_1, 111.1, 222.2);
    }

    @Test
    @DisplayName("Add Training Of User - Should throw TrainingOnDateAlreadyExistsException if training on date already exists")
    void addTrainingOfUser_shouldThrowTrainingOnDateAlreadyExistsExceptionIfTrainingOnDateAlreadyExists() throws EmptyException, UserNotFoundException, TrainingOnDateAlreadyExistsException {
        when(typeOfTrainingDAO.getByType(TYPE_1)).thenReturn(Optional.ofNullable(TYPE_OF_TRAINING_1));
        trainingsService.addTrainingOfUser(LOGIN, TYPE_1, DATE_1, 111.1, 222.2);
        when(TRAINING_1.date()).thenReturn(DATE_1);
        when(trainingOfUserDAO.getByLogin(LOGIN)).thenReturn(List.of(TRAINING_1));

        assertThatThrownBy(() ->
                trainingsService.addTrainingOfUser(LOGIN, TYPE_1, DATE_1, 111.1, 222.2))
                .isInstanceOf(TrainingOnDateAlreadyExistsException.class);
    }

    @Test
    @DisplayName("Get All Trainings For User - Should return all trainings for user")
    void getAllTrainingsForUser_shouldReturnAllTrainingsForUser() throws EmptyException {
        List<Training> mockTrainings = new ArrayList<>(List.of(TRAINING_1, TRAINING_2));
        when(TRAINING_1.date()).thenReturn(DATE_1);
        when(TRAINING_2.date()).thenReturn(DATE_2);
        when(trainingOfUserDAO.getByLogin(LOGIN)).thenReturn(mockTrainings);
        when(typeOfTrainingDAO.getById(TRAINING_1.id())).thenReturn(Optional.ofNullable(TYPE_OF_TRAINING_1));
        when(typeOfTrainingDAO.getById(TRAINING_2.id())).thenReturn(Optional.ofNullable(TYPE_OF_TRAINING_2));

        List<TrainingOfUser> result = trainingsService.getAllTrainingsForUser(LOGIN);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Get All Trainings For User By Type Of Training - Should return all trainings for user by type of training")
    void getAllTrainingsForUserByTypeOfTraining_shouldReturnAllTrainingsForUserByTypeOfTraining() throws EmptyException {
        long typeOfTrainingId = 1L;
        List<Training> mockTrainings = new ArrayList<>(List.of(TRAINING_1, TRAINING_2));
        when(TRAINING_1.date()).thenReturn(DATE_1);
        when(TRAINING_2.date()).thenReturn(DATE_2);
        when(TRAINING_1.id()).thenReturn(1L);
        when(TRAINING_2.id()).thenReturn(2L);
        when(typeOfTrainingDAO.getById(TRAINING_1.id())).thenReturn(Optional.ofNullable(TYPE_OF_TRAINING_1));
        when(typeOfTrainingDAO.getById(TRAINING_2.id())).thenReturn(Optional.ofNullable(TYPE_OF_TRAINING_2));
        when(TRAINING_1.typeOfTrainingId()).thenReturn(1L);
        when(TRAINING_2.typeOfTrainingId()).thenReturn(2L);

        when(trainingOfUserDAO.getAllTrainingsForUserByTypeOfTraining(LOGIN, typeOfTrainingId)).thenReturn(mockTrainings);

        List<TrainingOfUser> result = trainingsService.getAllTrainingsForUserByTypeOfTraining(LOGIN, typeOfTrainingId);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).training().id()).isEqualTo(1L);
        assertThat(result.get(1).training().id()).isEqualTo(2L);
    }

    @Test
    @DisplayName("Edit Training - Should edit training")
    void edite_shouldEditTraining() throws EmptyException {
        long idOfTrainingForEdit = 1L;
        TrainingOfUser newTrainingOfUser = TRAINING_OF_USER_2;
        when(newTrainingOfUser.login()).thenReturn(LOGIN);
        when(newTrainingOfUser.training()).thenReturn(TRAINING_2);
        when(newTrainingOfUser.training().date()).thenReturn(DATE_2);
        when(newTrainingOfUser.training().typeOfTrainingId()).thenReturn(2L);
        when(newTrainingOfUser.training().duration()).thenReturn(200.0);
        when(newTrainingOfUser.training().caloriesBurned()).thenReturn(100.0);

        when(TRAINING_1.date()).thenReturn(DATE_1);
        List<Training> mockTrainingList = List.of(TRAINING_1);
        when(trainingOfUserDAO.getByLogin(LOGIN)).thenReturn(mockTrainingList);

        assertThatCode(() -> trainingsService.editTraining(idOfTrainingForEdit, newTrainingOfUser))
                .doesNotThrowAnyException();

        verify(trainingOfUserDAO, times(1)).edit(
                idOfTrainingForEdit,
                newTrainingOfUser.training().typeOfTrainingId(),
                newTrainingOfUser.training().date(),
                newTrainingOfUser.training().duration(),
                newTrainingOfUser.training().caloriesBurned());
    }

    @Test
    @DisplayName("Get Statistic - Should calculate statistics for trainings")
    void getStatistic_shouldCalculateStatisticsForTrainings() {
        List<TrainingOfUser> trainingOfUserList = new ArrayList<>();
        trainingOfUserList.add(TRAINING_OF_USER_1);
        trainingOfUserList.add(TRAINING_OF_USER_2);

        when(TRAINING_OF_USER_1.training()).thenReturn(TRAINING_1);
        when(TRAINING_OF_USER_2.training()).thenReturn(TRAINING_2);
        when(TRAINING_OF_USER_1.training().date()).thenReturn(DATE_1);
        when(TRAINING_OF_USER_2.training().date()).thenReturn(DATE_2);

        when(TRAINING_1.typeOfTrainingId()).thenReturn(1L);
        when(TRAINING_1.date()).thenReturn(DATE_1);
        when(TRAINING_1.duration()).thenReturn(100.0);
        when(TRAINING_1.caloriesBurned()).thenReturn(1000.0);

        when(TRAINING_2.typeOfTrainingId()).thenReturn(2L);
        when(TRAINING_2.date()).thenReturn(DATE_2);
        when(TRAINING_2.duration()).thenReturn(200.0);
        when(TRAINING_2.caloriesBurned()).thenReturn(2000.0);

        Statistic statisticDuration = trainingsService.getStatistic(trainingOfUserList, 1);
        Statistic statisticCaloriesBurned = trainingsService.getStatistic(trainingOfUserList, 2);

        assertThat(statisticDuration).isNotNull();
        assertThat(statisticDuration.minValue()).isEqualTo(100.0);
        assertThat(statisticDuration.maxValue()).isEqualTo(200.0);
        assertThat(statisticDuration.totalValue()).isEqualTo(300.0);
        assertThat(statisticDuration.averageValue()).isEqualTo(150.0);

        assertThat(statisticCaloriesBurned).isNotNull();
        assertThat(statisticCaloriesBurned.minValue()).isEqualTo(1000.0);
        assertThat(statisticCaloriesBurned.maxValue()).isEqualTo(2000.0);
        assertThat(statisticCaloriesBurned.totalValue()).isEqualTo(3000.0);
        assertThat(statisticCaloriesBurned.averageValue()).isEqualTo(1500.0);
    }

    @Test
    @DisplayName("Delete - Should delete training")
    void delete_shouldDeleteTraining() {
        long trainingId = 1L;

        trainingsService.deleteTraining(trainingId);

        verify(trainingOfUserDAO, times(1)).delete(trainingId);
    }

    @Test
    @DisplayName("Edit Additional Info - Should edit additional info of training")
    void editAdditionalInfo_shouldEditAdditionalInfoOfTraining() throws EmptyException {
        long trainingId = 1L;
        String info = "info";
        Double value = 100.0;

        Map<String, Double> mockAdditionalInfo = new HashMap<>();
        mockAdditionalInfo.put(info, value);

        when(trainingOfUserDAO.getAdditionalInformation(trainingId)).thenReturn(mockAdditionalInfo);

        trainingsService.editAdditionalInfo(trainingId, info, value);

        verify(trainingOfUserDAO, times(1)).addAdditionalInformation(trainingId, mockAdditionalInfo);
    }
}
