package dev.makeev.training_diary_app.repository;

import dev.makeev.training_diary_app.model.Training;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@DisplayName("TrainingOfUserRepository Test")
@ExtendWith(MockitoExtension.class)
class TrainingOfUserRepositoryTest {

    private static final String LOGIN = "TestLogin";
    private final static String WRONG_TEST_LOGIN = "WrongTestLogin";


    private TrainingOfUserRepository trainingRepository;

    @BeforeEach
    void setUp() {
        trainingRepository = new TrainingOfUserRepository();
    }

    @Test
    @DisplayName("Adding Training - Should save Training")
    void addTraining_shouldSaveTraining() {
        final Training testTraining1 = mock(Training.class);
        when(testTraining1.date()).thenReturn(LocalDate.now());
        final Training testTraining2 = mock(Training.class);

        Map<String, List<Training>> trainings = trainingRepository.getAll();
        int sizeBeforeAdd = trainings.size();
        trainingRepository.add(LOGIN, testTraining1);
        trainingRepository.add(LOGIN, testTraining2);
        int sizeAfterAdd = trainings.size();

        assertNotNull(trainings);
        Assertions.assertThat(trainings).hasSize(sizeAfterAdd - sizeBeforeAdd);
        assertThat(trainings.containsKey(LOGIN)).isTrue();
        assertThat(trainings.get(LOGIN).get(0).date()).isEqualTo(LocalDate.now());
        assertThat(trainings.get(LOGIN).get(0)).isEqualTo(testTraining1);
        Assertions.assertThat(trainings.get(LOGIN)).hasSize(2);
    }

    @Test
    @DisplayName("Get Training by login - Should get Training by login")
    void getTrainingByLogin_shouldGetTrainingByLogin() {
        final Training testTraining1 = mock(Training.class);
        final Training testTraining2 = mock(Training.class);

        trainingRepository.add(LOGIN, testTraining1);
        trainingRepository.add(LOGIN, testTraining2);

        List<Training> trainings = trainingRepository.getBy(LOGIN);

        assertNotNull(trainings);
        Assertions.assertThat(trainings).hasSize(2);
        assertThat(trainings.get(0)).isEqualTo(testTraining1);
        assertThat(trainings.get(1)).isEqualTo(testTraining2);
        assertThat(trainingRepository.getBy(WRONG_TEST_LOGIN).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Get All Trainings - Should get all Trainings")
    void getAllTrainings_shouldGetAllTrainings() {
        final Training testTraining1 = mock(Training.class);
        final Training testTraining2 = mock(Training.class);

        trainingRepository.add(LOGIN, testTraining1);
        trainingRepository.add(LOGIN, testTraining2);

        Map<String, List<Training>> allTrainings = trainingRepository.getAll();
        List<Training> trainings = allTrainings.get(LOGIN);

        assertNotNull(trainings);
        Assertions.assertThat(trainings).hasSize(2);
        assertThat(trainings.contains(testTraining1)).isTrue();
        assertThat(trainings.contains(testTraining2)).isTrue();
    }

    @Test
    @DisplayName("Delete Training - Should delete Training by index")
    void deleteTraining_shouldDeleteTraining() {
        final Training testTraining1 = mock(Training.class);
        final Training testTraining2 = mock(Training.class);

        trainingRepository.add(LOGIN, testTraining1);
        trainingRepository.add(LOGIN, testTraining2);

        trainingRepository.delete(0, LOGIN);

        List<Training> trainings = trainingRepository.getBy(LOGIN);

        assertNotNull(trainings);
        Assertions.assertThat(trainings).hasSize(1);
        assertThat(trainings.contains(testTraining1)).isFalse();
        assertThat(trainings.contains(testTraining2)).isTrue();
    }

    @Test
    @DisplayName("Edit Training - Should edit Training by index")
    void editTraining_shouldEditTraining() {
        final Training testTraining1 = mock(Training.class);
        final Training testTraining2 = mock(Training.class);
        final Training updatedTraining = mock(Training.class);

        trainingRepository.add(LOGIN, testTraining1);
        trainingRepository.add(LOGIN, testTraining2);

        trainingRepository.edit(0, LOGIN, updatedTraining);

        List<Training> trainings = trainingRepository.getBy(LOGIN);

        assertNotNull(trainings);
        Assertions.assertThat(trainings).hasSize(2);
        assertThat(trainings.get(0)).isEqualTo(updatedTraining);
        assertThat(trainings.get(1)).isEqualTo(testTraining2);
    }
}