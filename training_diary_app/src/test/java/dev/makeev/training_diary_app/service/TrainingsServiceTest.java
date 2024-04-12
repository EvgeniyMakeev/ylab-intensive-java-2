package dev.makeev.training_diary_app.service;

import dev.makeev.training_diary_app.dao.TrainingOfUserDAO;
import dev.makeev.training_diary_app.dao.TypeOfTrainingDAO;
import dev.makeev.training_diary_app.exceptions.EmptyException;
import dev.makeev.training_diary_app.exceptions.TrainingOnDateAlreadyExistsException;
import dev.makeev.training_diary_app.exceptions.UserNotFoundException;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("TrainingsService Test")
@ExtendWith(MockitoExtension.class)
class TrainingsServiceTest {

    @Mock
    private TrainingOfUserDAO trainingOfUserDAO;

    @Mock
    private TypeOfTrainingDAO typeOfTrainingDAO;

    @InjectMocks
    private TrainingsService trainingsService;

    @Test
    @DisplayName("Get All Types Of Training - Should return list of all types of training")
    void getAllTypesOfTraining_shouldReturnListOfAllTypesOfTraining() {
        List<TypeOfTraining> mockTypes = List.of(new TypeOfTraining("Type1"), new TypeOfTraining("Type2"));
        Mockito.when(typeOfTrainingDAO.getAll()).thenReturn(mockTypes);

        List<TypeOfTraining> result = trainingsService.getAllTypesOfTraining();

        assertThat(result).isEqualTo(mockTypes);
        Mockito.verify(typeOfTrainingDAO, Mockito.times(1)).getAll();
    }

    @Test
    @DisplayName("Get Type Of Training By Index - Should return type of training by index")
    void getTypeOfTrainingByIndex_shouldReturnTypeOfTrainingByIndex() throws EmptyException {
        TypeOfTraining mockType = new TypeOfTraining("Type1");
        Mockito.when(typeOfTrainingDAO.getByIndex(0)).thenReturn(Optional.of(mockType));

        String result = trainingsService.getTypeOfTrainingByIndex(0);

        assertEquals("Type1", result);
        Mockito.verify(typeOfTrainingDAO, Mockito.times(1)).getByIndex(0);
    }

    @Test
    @DisplayName("Add Type Of Training - Should call DAO add method with correct parameter")
    void addTypeOfTraining_shouldCallDAOAddMethodWithCorrectParameter() {
        trainingsService.addTypeOfTraining("NewType");

        Mockito.verify(typeOfTrainingDAO, Mockito.times(1)).add("NewType");
    }

    @Test
    @DisplayName("Add Training Of User - Should add training for user")
    void addTrainingOfUser_shouldAddTrainingForUser() throws EmptyException, UserNotFoundException, TrainingOnDateAlreadyExistsException {
        Mockito.when(typeOfTrainingDAO.getBy("Type1")).thenReturn(new TypeOfTraining("Type1"));
        Mockito.when(trainingOfUserDAO.getByLogin("DemoUser")).thenReturn(List.of());

        trainingsService.addTrainingOfUser("DemoUser", "Type1", LocalDate.of(2024, 12, 2), 100, 500);

        Mockito.verify(trainingOfUserDAO, Mockito.times(1)).add("DemoUser", new TypeOfTraining("Type1"), LocalDate.of(2024, 12, 2), 100, 500);
    }

    @Test
    @DisplayName("Add Training Of User - Should throw TrainingOnDateAlreadyExistsException if training on date already exists")
    void addTrainingOfUser_shouldThrowTrainingOnDateAlreadyExistsExceptionIfTrainingOnDateAlreadyExists() {
        Mockito.when(trainingOfUserDAO.getByLogin("DemoUser")).
                thenReturn(List.of(new Training(new TypeOfTraining("Type1"), LocalDate.of(2222, 2, 2), 10.0, 50.0)));

        assertThrows(TrainingOnDateAlreadyExistsException.class, () ->
                trainingsService.addTrainingOfUser("DemoUser", "Type1", LocalDate.of(2222, 2, 2), 10.0, 50.0));
    }
}
