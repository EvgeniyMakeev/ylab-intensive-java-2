package dev.makeev.training_diary_app.dao.impl;

import dev.makeev.training_diary_app.model.Training;
import dev.makeev.training_diary_app.model.TypeOfTraining;
import dev.makeev.training_diary_app.repository.TrainingOfUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DisplayName("TrainingOfUserDAO Test")
@ExtendWith(MockitoExtension.class)
class TrainingOfUserDAOImplTest {

    private static final String LOGIN = "TestLogin";

    @Mock
    private TrainingOfUserRepository trainingOfUserRepository;

    @InjectMocks
    private TrainingOfUserDAOImpl trainingDAO;

    @Test
    @DisplayName("Adding Training - Should save Training to repository")
    void addTraining_shouldSaveTrainingToRepository() {
        final TypeOfTraining testTypeOfTraining = Mockito.mock(TypeOfTraining.class);
        final LocalDate localDate = LocalDate.now();

        trainingDAO.add(LOGIN, testTypeOfTraining, localDate, 30.0, 200.0);

        Mockito.verify(trainingOfUserRepository, Mockito.times(1)).add(Mockito.eq(LOGIN), Mockito.any(Training.class));
    }

    @Test
    @DisplayName("Get Trainings by login - Should get Training by login from repository")
    void getTrainingsByLogin_shouldGetTrainingByLoginFromRepository() {
        final Training testTraining1 = Mockito.mock(Training.class);
        final Training testTraining2 = Mockito.mock(Training.class);
        List<Training> mockTrainings = new ArrayList<>();
        mockTrainings.add(testTraining1);
        mockTrainings.add(testTraining2);

        Mockito.when(trainingOfUserRepository.getBy(LOGIN)).thenReturn(mockTrainings);

        trainingDAO.getByLogin(LOGIN);

        Mockito.verify(trainingOfUserRepository, Mockito.times(1)).getBy(Mockito.eq(LOGIN));
    }

    @Test
    @DisplayName("Get All Trainings - Should get all Trainings from repository")
    void getAllTrainings_shouldGetAllTrainingsFromRepository() {
        final Training testTraining1 = Mockito.mock(Training.class);
        final Training testTraining2 = Mockito.mock(Training.class);
        List<Training> mockTrainings = new ArrayList<>();
        mockTrainings.add(testTraining1);
        mockTrainings.add(testTraining2);
        Map<String, List<Training>> mockMap = new HashMap<>();
        mockMap.put(LOGIN, mockTrainings);

        Mockito.when(trainingOfUserRepository.getAll()).thenReturn(mockMap);

        trainingDAO.getAll();

        Mockito.verify(trainingOfUserRepository, Mockito.times(1)).getAll();
    }


    @Test
    @DisplayName("Delete Training - Should delete Training by index from repository")
    void deleteTraining_shouldDeleteTrainingFromRepository() {
        trainingDAO.delete(0, LOGIN);

        Mockito.verify(trainingOfUserRepository, Mockito.times(1)).delete(Mockito.eq(0), Mockito.eq(LOGIN));
    }

    @Test
    @DisplayName("Edit Training - Should edit existing training")
    void edit_shouldEditExistingTraining() {
        String login = "testUser";
        TypeOfTraining oldType = new TypeOfTraining("OldType");
        TypeOfTraining newType = new TypeOfTraining("NewType");
        LocalDate oldDate = LocalDate.of(2022, 1, 1);
        LocalDate newDate = LocalDate.of(2022, 1, 2);
        double oldDuration = 30.0;
        double newDuration = 45.0;
        double oldCaloriesBurned = 200.0;
        double newCaloriesBurned = 250.0;

        Training oldTraining = new Training(oldType, oldDate, oldDuration, oldCaloriesBurned);
        Training editedTraining = new Training(newType, newDate, newDuration, newCaloriesBurned);

        Mockito.when(trainingOfUserRepository.getAll()).thenReturn(Map.of(login, List.of(oldTraining)));

        trainingDAO.edit(0, login, newType, newDate, newDuration, newCaloriesBurned);

        Mockito.verify(trainingOfUserRepository, Mockito.times(1)).edit(0, login, editedTraining);
    }

}