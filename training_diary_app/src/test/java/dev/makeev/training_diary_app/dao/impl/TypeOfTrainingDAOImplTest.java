package dev.makeev.training_diary_app.dao.impl;

import dev.makeev.training_diary_app.exceptions.EmptyException;
import dev.makeev.training_diary_app.model.TypeOfTraining;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("TypeOfTrainingDAO Test")
class TypeOfTrainingDAOImplTest {

    private static final String TYPE = "TestType";
    private static final int INDEX = 25;

    private TypeOfTrainingDAOImpl typeOfTrainingDAO;

    @BeforeEach
    public void setUp() {
        typeOfTrainingDAO = new TypeOfTrainingDAOImpl();
    }

    @Test
    @DisplayName("Add Type - Should add type")
    void addType_shouldAddType() throws EmptyException {
        List<TypeOfTraining> typesOfTraining = typeOfTrainingDAO.getAll();
        int sizeBeforeAdd = typesOfTraining.size();
        typeOfTrainingDAO.add(TYPE);
        TypeOfTraining result = typeOfTrainingDAO.getByType(TYPE);

        assertThat(typesOfTraining).hasSize(sizeBeforeAdd + 1);
        assertThat(result.type()).isEqualTo(TYPE);
    }

    @Test
    @DisplayName("Get By Type - Should get type by type")
    void getByType_shouldGetTypeByType() throws EmptyException {
        List<TypeOfTraining> typesOfTraining = typeOfTrainingDAO.getAll();
        int sizeBeforeAdd = typesOfTraining.size();
        typeOfTrainingDAO.add(TYPE);
        TypeOfTraining result = typeOfTrainingDAO.getByType(TYPE);

        assertThat(typesOfTraining).hasSize(sizeBeforeAdd + 1);
        assertThat(result.type()).isEqualTo(TYPE);
    }

    @Test
    @DisplayName("Get By Type - Should throw EmptyException when type not found")
    void getByType_shouldThrowEmptyExceptionWhenTypeNotFound() {
        typeOfTrainingDAO.add(TYPE);

        assertThatCode(() -> typeOfTrainingDAO.getByType(TYPE)).doesNotThrowAnyException();
        assertThatExceptionOfType(EmptyException.class)
                .isThrownBy(() -> typeOfTrainingDAO.getByType("Wrong Type"));
    }

    @Test
    @DisplayName("Get All Types - Should get all types")
    void getAll_shouldGetAllTypes() {
        List<TypeOfTraining> typesOfTraining = typeOfTrainingDAO.getAll();

        assertThat(typesOfTraining).hasSize(3);
        assertThat(typesOfTraining.get(0).type()).isEqualToIgnoringCase("Cardio training");
        assertThat(typesOfTraining.get(1).type()).isEqualToIgnoringCase("Power training");
        assertThat(typesOfTraining.get(2).type()).isEqualToIgnoringCase("Yoga");
    }

    @Test
    @DisplayName("Get By Index - Should throw EmptyException when index out of bounds")
    void getByIndex_shouldThrowEmptyExceptionWhenIndexOutOfBounds() {
        assertThatExceptionOfType(EmptyException.class)
                .isThrownBy(() -> typeOfTrainingDAO.getByIndex(INDEX));
    }
}
