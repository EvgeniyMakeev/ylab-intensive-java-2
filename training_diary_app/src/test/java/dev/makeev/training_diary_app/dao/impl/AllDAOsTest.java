package dev.makeev.training_diary_app.dao.impl;

import dev.makeev.training_diary_app.dao.LogEventDAO;
import dev.makeev.training_diary_app.dao.TrainingOfUserDAO;
import dev.makeev.training_diary_app.dao.TypeOfTrainingDAO;
import dev.makeev.training_diary_app.dao.UserDAO;
import dev.makeev.training_diary_app.exceptions.EmptyException;
import dev.makeev.training_diary_app.model.Training;
import dev.makeev.training_diary_app.model.TypeOfTraining;
import dev.makeev.training_diary_app.model.User;
import dev.makeev.training_diary_app.model.LogEvent;
import dev.makeev.training_diary_app.utils.ConnectionManager;
import dev.makeev.training_diary_app.utils.ConnectionManagerImpl;
import dev.makeev.training_diary_app.utils.InitDB;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

@Testcontainers
@DisplayName("Tests for all DAO")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AllDAOsTest {
    public final static String LOGIN = "TestLogin";
    public static final String PASSWORD = "TestPassword";
    public final static String WRONG_LOGIN = "WrongTestLogin";
    public static final String TYPE = "TestType";
    public static final LocalDate DATE = LocalDate.now();
    public static final String MESSAGE = "TestMessage";
    public static final long TYPE_OF_TRAINING_ID = 1L;
    public static final double DURATION = 60.0;
    public static final double CALORIES_BURNED = 500.0;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:16.2");
    private static UserDAO userDAO;
    private static TypeOfTrainingDAO typeOfTrainingDAO;
    private static TrainingOfUserDAO trainingOfUserDAO;
    private static LogEventDAO logEventDAO;

    @BeforeAll
    static void setUpAll() {
        postgresContainer.start();

        String jdbcUrl = postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();

        ConnectionManager testConnectionManager = new ConnectionManagerImpl(jdbcUrl, username, password);

        InitDB initDB = new InitDB(testConnectionManager);
        initDB.initDB();

        userDAO = new UserDAOImpl(testConnectionManager);
        typeOfTrainingDAO = new TypeOfTrainingDAOImpl(testConnectionManager);
        trainingOfUserDAO = new TrainingOfUserDAOImpl(testConnectionManager);
        logEventDAO = new LogEventDAOImpl(testConnectionManager);
    }

    @AfterAll
    static void afterAll() {
        postgresContainer.stop();
    }

    @Test
    @Order(1)
    @DisplayName("UserDAOImpl test: Add User - Should add new user")
    void add_shouldAddUser() {
        List<User> users = userDAO.getAll();
        int sizeBeforeAdd = users.size();
        userDAO.add(LOGIN, PASSWORD);
        List<User> usersAfterAdd = userDAO.getAll();

        assertThat(usersAfterAdd).hasSize(sizeBeforeAdd + 1);
        assertThat(userDAO.getByLogin(LOGIN)).isPresent();
    }

    @Test
    @Order(2)
    @DisplayName("UserDAOImpl test: Get User by login - Success")
    void getBy_shouldGetUser_whenExists() {
        Optional<User> user = userDAO.getByLogin(LOGIN);

        assertThat(user).isPresent();
        assertThat(user.get().password()).isEqualTo(PASSWORD);
    }

    @Test
    @Order(3)
    @DisplayName("UserDAOImpl test: Get By Login - Should return empty optional if user does not exist")
    void getBy_shouldReturnEmptyOptionalIfUserDoesNotExist() {
        Optional<User> result = userDAO.getByLogin(WRONG_LOGIN);

        assertThat(result).isEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("UserDAOImpl test: Get All Users - Should return all users")
    void getAll_shouldReturnAllUsers() {
        List<User> users = userDAO.getAll();

        assertThat(users).isNotNull();
        assertThat(userDAO.getByLogin(LOGIN)).isPresent();
    }

    @Test
    @Order(5)
    @DisplayName("TypeOfTrainingDAOImpl test: Add Type - Should add type")
    void addType_shouldAddType() {
        List<TypeOfTraining> typesOfTrainingBeforeAdd = typeOfTrainingDAO.getAll();
        typeOfTrainingDAO.add(TYPE);
        List<TypeOfTraining> typesOfTrainingAfterAdd = typeOfTrainingDAO.getAll();

        assertThat(typesOfTrainingAfterAdd.size())
                .isEqualTo(typesOfTrainingBeforeAdd.size() + 1);
    }

    @Test
    @Order(6)
    @DisplayName("TypeOfTrainingDAOImpl test: Get By Type - Should get type by type")
    void getByType_shouldGetTypeByType() {
        TypeOfTraining result = typeOfTrainingDAO.getByType(TYPE).orElse(null);

        assertThat(result).isNotNull();
        assertThat(result.type()).isEqualTo(TYPE);
    }

    @Test
    @Order(7)
    @DisplayName("TypeOfTrainingDAOImpl test: Get By Type - Should get type by ID")
    void getById_shouldGetTypeById() throws EmptyException {
        TypeOfTraining result = typeOfTrainingDAO.getById(1).orElse(null);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1);
    }

    @Test
    @Order(8)
    @DisplayName("TypeOfTrainingDAOImpl test: Get By Type - Should get Type of training by string type")
    void getByType_shouldGetTypeOfTrainingByStringType() {
        Optional<TypeOfTraining> typeOfTraining = typeOfTrainingDAO.getByType(TYPE);

        assertThat(typeOfTraining).isPresent();
        assertThat(typeOfTraining.get().type()).isEqualTo(TYPE);
        assertThat(typeOfTrainingDAO.getByType("Wrong Type")).isEmpty();
    }

    @Test
    @Order(9)
    @DisplayName("TypeOfTrainingDAOImpl test: Get All Types - Should get all types")
    void getAll_shouldGetAllTypes() {
        List<TypeOfTraining> typesOfTraining = typeOfTrainingDAO.getAll();

        assertThat(typesOfTraining).isNotNull();
        assertThat(typesOfTraining).isNotEmpty();
        assertThat(typesOfTraining.get(0).type()).isEqualToIgnoringCase("Cardio training");
        assertThat(typesOfTraining.get(1).type()).isEqualToIgnoringCase("Power training");
        assertThat(typesOfTraining.get(2).type()).isEqualToIgnoringCase("Yoga");
    }

    @Test
    @Order(10)
    @DisplayName("TrainingOfUserDAOImpl test: Add Training - Should save Training")
    void addTraining_shouldSaveTraining() throws EmptyException {
        trainingOfUserDAO.add(LOGIN, TYPE_OF_TRAINING_ID, DATE, DURATION, CALORIES_BURNED);
        List<Training> trainingsAfterAdd = trainingOfUserDAO.getByLogin(LOGIN);

        assertThat(trainingsAfterAdd).isNotNull();
        assertThat(trainingsAfterAdd.stream()
                        .anyMatch(t -> t.date().isEqual(DATE)))
                .isTrue();
    }

    @Test
    @Order(11)
    @DisplayName("TrainingOfUserDAOImpl test: Get Trainings by login - Should get Training by login")
    void getTrainingsByLogin_shouldGetTrainingByLogin() throws EmptyException {
        List<Training> trainings = trainingOfUserDAO.getByLogin(LOGIN);

        assertThat(trainings).isNotNull();
        assertThatCode(() -> trainingOfUserDAO.getByLogin(LOGIN))
                .doesNotThrowAnyException();
    }

    @Test
    @Order(12)
    @DisplayName("TrainingOfUserDAOImpl test: Get All Trainings for User by Type of Training - Should get all Trainings for User by Type of Training")
    void getAllTrainingsForUserByTypeOfTraining_shouldGetAllTrainingsForUserByTypeOfTraining() {
        List<Training> trainings =
                trainingOfUserDAO.getAllTrainingsForUserByTypeOfTraining(LOGIN, TYPE_OF_TRAINING_ID);

        assertThat(trainings).isNotNull();
        Training training = trainings.get(0);
        assertThat(TYPE_OF_TRAINING_ID).isEqualTo(training.typeOfTrainingId());
    }

    @Test
    @Order(13)
    @DisplayName("TrainingOfUserDAOImpl test: Add Additional Information for Training - Should add additional information for Training")
    void addAdditionalInformation_shouldAddAdditionalInformation() throws EmptyException {
        Map<String, Double> additionalInfo = new HashMap<>();
        additionalInfo.put("distance", 5.0);
        additionalInfo.put("speed", 10.0);

        Training training = trainingOfUserDAO.getByLogin(LOGIN).get(0);
        long trainingId = training.id();

        trainingOfUserDAO.addAdditionalInformation(trainingId, additionalInfo);
        Map<String, Double> retrievedAdditionalInfo = trainingOfUserDAO.getAdditionalInformation(trainingId);

        assertThat(retrievedAdditionalInfo.isEmpty()).isFalse();
        Assertions.assertThat(retrievedAdditionalInfo).hasSize(2);
        assertThat(retrievedAdditionalInfo.get("distance")).isEqualTo(5.0);
        assertThat(retrievedAdditionalInfo.get("speed")).isEqualTo(10.0);
    }

    @Test
    @Order(14)
    @DisplayName("TrainingOfUserDAOImpl test: Edit Training - Should edit existing training")
    void edit_shouldEditExistingTraining() throws EmptyException {
        long newTypeOfTrainingId = TYPE_OF_TRAINING_ID + 1L;
        LocalDate newDate = LocalDate.of(2022, 1, 2);
        double newDuration = 70.0;
        double newCaloriesBurned = 600.0;

        Training training =
                trainingOfUserDAO.getByLogin(LOGIN).get(0);
        long idOfTrainingForEdite = training.id();

        trainingOfUserDAO.edit(
                idOfTrainingForEdite, newTypeOfTrainingId,
                newDate, newDuration, newCaloriesBurned);

        List<Training> trainings = trainingOfUserDAO.getByLogin(LOGIN);
        Training editedTraining = trainings.stream()
                .filter(t -> t.id() == idOfTrainingForEdite)
                .findFirst()
                .orElse(null);

        assertThat(editedTraining).isNotNull();
        assertThat(editedTraining.typeOfTrainingId()).isEqualTo(newTypeOfTrainingId);
        assertThat(editedTraining.date()).isEqualTo(newDate);
        assertThat(editedTraining.duration()).isEqualTo(newDuration);
        assertThat(editedTraining.caloriesBurned()).isEqualTo(newCaloriesBurned);
    }

    @Test
    @Order(15)
    @DisplayName("TrainingOfUserDAOImpl test: Delete Training - Should delete Training by id")
    void delete_shouldDeleteTrainingById() throws EmptyException {
        Training trainingForDelete =
                trainingOfUserDAO.getByLogin(LOGIN).get(0);
        long trainingId = trainingForDelete.id();

        List<Training> trainingsBeforeDelete = trainingOfUserDAO.getByLogin(LOGIN);
        trainingOfUserDAO.delete(trainingId);

        assertThat(trainingsBeforeDelete).isNotNull();
        assertThatExceptionOfType(EmptyException.class)
                .isThrownBy(() -> trainingOfUserDAO.getByLogin(LOGIN));
    }

    @Test
    @Order(16)
    @DisplayName("LogEventDAOImpl test: Add Log Event - Should add log event")
    void addLogEvent_shouldAddLogEvent() throws EmptyException {
        logEventDAO.addEvent(LOGIN, MESSAGE);
        List<LogEvent> eventsAfterAdd = logEventDAO.getAllEvents();

        assertThat(eventsAfterAdd).isNotNull();
        assertThat(eventsAfterAdd).isNotEmpty();
        assertThat(eventsAfterAdd.get(0).date()).isEqualTo(DATE);
        assertThat(eventsAfterAdd.get(0).login()).isEqualTo(LOGIN);
        assertThat(eventsAfterAdd.get(0).message()).isEqualTo(MESSAGE);
    }

    @Test
    @Order(17)
    @DisplayName("LogEventDAOImpl test: Get All Log Events - Should get all log events from repository")
    void getAllLogEvents_shouldGetAllLogEventsFromRepository() throws EmptyException {
        List<LogEvent> events = logEventDAO.getAllEvents();

        assertThat(events).isNotNull();
        assertThat(events).isNotEmpty();
        assertThat(events.get(0).date()).isEqualTo(DATE);
        assertThat(events.get(0).login()).isEqualTo(LOGIN);
        assertThat(events.get(0).message()).isEqualTo(MESSAGE);
    }

    @Test
    @Order(18)
    @DisplayName("LogEventDAOImpl test: Get All Log Events For User - Should get all log events for user")
    void getAllLogEventsForUser_shouldGetAllLogEventsForUser() throws EmptyException {
        List<LogEvent> events = logEventDAO.getAllEventsForUser(LOGIN);

        assertThat(events).isNotNull();
        assertThat(events).isNotEmpty();
        assertThat(events.get(0).login()).isEqualTo(LOGIN);
    }

    @Test
    @Order(19)
    @DisplayName("LogEventDAOImpl test: Get All Log Events For User - Should throw EmptyException if user not found")
    void getAllLogEventsForUser_shouldThrowEmptyExceptionIfUserNotFound(){
        assertThatExceptionOfType(EmptyException.class)
                .isThrownBy(() -> logEventDAO.getAllEventsForUser(WRONG_LOGIN));
    }
}
