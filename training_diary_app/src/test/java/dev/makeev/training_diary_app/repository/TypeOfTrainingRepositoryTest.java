package dev.makeev.training_diary_app.repository;

import dev.makeev.training_diary_app.model.TypeOfTraining;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("TypeOfTrainingRepository Test")
class TypeOfTrainingRepositoryTest {

    private TypeOfTrainingRepository typeOfTrainingRepository;
    private String testType;
    private TypeOfTraining typeOfTrainingTest;

    @BeforeEach
    void setUp() {
        typeOfTrainingRepository = new TypeOfTrainingRepository();
        testType = "TestType";
        typeOfTrainingTest = new TypeOfTraining(testType);
    }

    @Test
    @DisplayName("Adding TypeOfTraining - Success")
    void addTypeOfTraining_Success() {
        List<TypeOfTraining> typeOfTrainings = typeOfTrainingRepository.getAll();
        int sizeBeforeAdd = typeOfTrainings.size();

        typeOfTrainingRepository.add(typeOfTrainingTest);

        assertThat(typeOfTrainingRepository.getAll().size()).isEqualTo(sizeBeforeAdd + 1);
        assertThat(typeOfTrainingRepository.getBy(testType)).isPresent();
        assertThat(typeOfTrainingRepository.getAll().contains(typeOfTrainingTest)).isTrue();
    }

    @Test
    @DisplayName("Getting by type of training - Success")
    void getByTypeOfTraining_Success() {
        typeOfTrainingRepository.add(typeOfTrainingTest);

        assertThat(typeOfTrainingRepository.getBy(testType)).isPresent();
        assertThat(typeOfTrainingRepository.getBy(testType).get()).isEqualTo(typeOfTrainingTest);
    }

    @Test
    @DisplayName("Getting all types of training - Success")
    void getAllTypesOfTraining_Success() {
        List<TypeOfTraining> typeOfTrainings = typeOfTrainingRepository.getAll();
        int sizeBeforeAdd = typeOfTrainings.size();

        typeOfTrainingRepository.add(typeOfTrainingTest);

        assertThat(typeOfTrainingRepository.getAll().size()).isEqualTo(sizeBeforeAdd + 1);
        assertThat(typeOfTrainingRepository.getAll().contains(typeOfTrainingTest)).isTrue();
    }
}