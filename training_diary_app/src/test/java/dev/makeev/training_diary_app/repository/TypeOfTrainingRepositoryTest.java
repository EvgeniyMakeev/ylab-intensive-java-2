package dev.makeev.training_diary_app.repository;

import dev.makeev.training_diary_app.model.TypeOfTraining;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("TypeOfTrainingRepository Test")
@ExtendWith(MockitoExtension.class)
class TypeOfTrainingRepositoryTest {

    private TypeOfTrainingRepository typeOfTrainingRepository;

    @BeforeEach
    void setUp() {
        typeOfTrainingRepository = new TypeOfTrainingRepository();
    }

    @Test
    @DisplayName("Adding TypeOfTraining - Should save TypeOfTraining")
    void addTypeOfTraining_shouldSaveTypeOfTraining() {
        final TypeOfTraining testTypeOfTraining = mock(TypeOfTraining.class);

        List<TypeOfTraining> typesOfTraining = typeOfTrainingRepository.getAll();
        int sizeBeforeAdd = typesOfTraining.size();
        typeOfTrainingRepository.add(testTypeOfTraining);

        assertThat(typesOfTraining).hasSize(sizeBeforeAdd + 1);
        assertThat(typesOfTraining.contains(testTypeOfTraining)).isTrue();
    }

    @Test
    @DisplayName("Get By TypeOfTraining - Should get TypeOfTraining by name")
    void getByTypeOfTraining_shouldGetTypeOfTrainingByName() {
        final TypeOfTraining testTypeOfTraining = mock(TypeOfTraining.class);
        when(testTypeOfTraining.type()).thenReturn("Cardio training");

        Optional<TypeOfTraining> typeOfTraining = typeOfTrainingRepository.getBy("Cardio training");

        assertTrue(typeOfTraining.isPresent());
        assertThat(typeOfTraining.get().type()).isEqualToIgnoringCase(testTypeOfTraining.type());
    }

    @Test
    @DisplayName("Get By TypeOfTraining - Should not find TypeOfTraining by wrong name")
    void getByTypeOfTraining_shouldNotFindTypeOfTrainingByWrongName() {
        Optional<TypeOfTraining> typeOfTraining = typeOfTrainingRepository.getBy("Wrong Type");

        assertFalse(typeOfTraining.isPresent());
    }

    @Test
    @DisplayName("Get All TypesOfTraining - Should get all TypesOfTraining")
    void getAllTypesOfTraining_shouldGetAllTypesOfTraining() {
        List<TypeOfTraining> typesOfTraining = typeOfTrainingRepository.getAll();

        assertThat(typesOfTraining).hasSize(3);
        assertThat(typesOfTraining.get(0).type()).isEqualToIgnoringCase("Cardio training");
        assertThat(typesOfTraining.get(1).type()).isEqualToIgnoringCase("Power training");
        assertThat(typesOfTraining.get(2).type()).isEqualToIgnoringCase("Yoga");
    }
}
