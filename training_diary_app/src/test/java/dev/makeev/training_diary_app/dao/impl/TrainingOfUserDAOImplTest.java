package dev.makeev.training_diary_app.dao.impl;

import dev.makeev.training_diary_app.model.Training;
import dev.makeev.training_diary_app.model.TypeOfTraining;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@DisplayName("TrainingOfUserDAO Test")
@ExtendWith(MockitoExtension.class)
class TrainingOfUserDAOImplTest {

    private final static String LOGIN = "TestLogin";
    private final static String WRONG_TEST_LOGIN = "WrongTestLogin";
    private final static TypeOfTraining TEST_TYPE_1 = mock(TypeOfTraining.class);
    private final static TypeOfTraining TEST_TYPE_2 = mock(TypeOfTraining.class);

    private TrainingOfUserDAOImpl trainingDAO;

    @BeforeEach
    public void setUp() {
        trainingDAO = new TrainingOfUserDAOImpl();
    }

    @Test
    @DisplayName("Adding Training - Should save Training")
    void addTraining_shouldSaveTraining() {
        List<Training> trainings = trainingDAO.getAll();
        int sizeBeforeAdd = trainings.size();
        trainingDAO.add(LOGIN, TEST_TYPE_1, LocalDate.now(), 100.0, 200.0);
        int sizeAfterAdd = trainings.size();

        assertNotNull(trainings);
        Assertions.assertThat(trainings).hasSize(sizeAfterAdd - sizeBeforeAdd);
    }

    @Test
    @DisplayName("Get Trainings by login - Should get Training by login")
    void getTrainingsByLogin_shouldGetTrainingByLogin() {
        trainingDAO.add(LOGIN, TEST_TYPE_1, LocalDate.now(), 100.0, 200.0);
        trainingDAO.add(LOGIN, TEST_TYPE_2, LocalDate.now().plusDays(1), 200.0, 300.0);
        List<Training> trainings = trainingDAO.getByLogin(LOGIN);

        assertNotNull(trainings);
        Assertions.assertThat(trainings).hasSize(2);
        assertThat(trainings.get(0).type()).isEqualTo(TEST_TYPE_1);
        assertThat(trainings.get(1).type()).isEqualTo(TEST_TYPE_2);
        assertThat(trainingDAO.getByLogin(WRONG_TEST_LOGIN).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Get All Trainings - Should get all Trainings")
    void getAllTrainings_shouldGetAllTrainings() {
        trainingDAO.add(LOGIN, TEST_TYPE_1, LocalDate.now(), 100.0, 200.0);
        trainingDAO.add(LOGIN, TEST_TYPE_2, LocalDate.now().plusDays(1), 200.0, 300.0);

        List<Training> trainings = trainingDAO.getAll();

        assertNotNull(trainings);
        Assertions.assertThat(trainings).hasSize(2);
        assertThat(trainings.get(0).type()).isEqualTo(TEST_TYPE_1);
        assertThat(trainings.get(1).type()).isEqualTo(TEST_TYPE_2);
    }


    @Test
    @DisplayName("Delete Training - Should delete Training by index")
    void deleteTraining_shouldDeleteTraining() {
        trainingDAO.add(LOGIN, TEST_TYPE_1, LocalDate.now(), 100.0, 200.0);
        trainingDAO.add(LOGIN, TEST_TYPE_2, LocalDate.now().plusDays(1), 200.0, 300.0);
        List<Training> trainingsBeforeDelete = trainingDAO.getAll();

        trainingDAO.delete(0, LOGIN);
        List<Training> trainingsAfterDelete = trainingDAO.getAll();

        Assertions.assertThat(trainingsAfterDelete).hasSize(trainingsBeforeDelete.size() - 1);
        assertThat(trainingDAO.getByLogin(LOGIN).size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Edit Training - Should edit existing training")
    void edit_shouldEditExistingTraining() {
        TypeOfTraining newType = TEST_TYPE_2;
        LocalDate oldDate = LocalDate.of(2022, 1, 1);
        LocalDate newDate = LocalDate.of(2022, 1, 2);
        double oldDuration = 30.0;
        double newDuration = 45.0;
        double oldCaloriesBurned = 200.0;
        double newCaloriesBurned = 250.0;

        trainingDAO.add(LOGIN, TEST_TYPE_1, oldDate, oldDuration, oldCaloriesBurned);
        trainingDAO.edit(0, LOGIN, newType, newDate, newDuration, newCaloriesBurned);
        Training editedTraining = trainingDAO.getByLogin(LOGIN).get(0);

        assertThat(editedTraining.type()).isEqualTo(newType);
        assertThat(editedTraining.date()).isEqualTo(newDate);
        assertThat(editedTraining.duration()).isEqualTo(newDuration);
        assertThat(editedTraining.caloriesBurned()).isEqualTo(newCaloriesBurned);
    }

}