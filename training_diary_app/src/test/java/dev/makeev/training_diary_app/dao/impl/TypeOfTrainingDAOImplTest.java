package dev.makeev.training_diary_app.dao.impl;

import dev.makeev.training_diary_app.exceptions.EmptyException;
import dev.makeev.training_diary_app.model.TypeOfTraining;
import dev.makeev.training_diary_app.repository.TypeOfTrainingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("TypeOfTrainingDAO Test")
@ExtendWith(MockitoExtension.class)
class TypeOfTrainingDAOImplTest {

    private static final String TYPE = "TestType";
    private static final int INDEX = 1;

    @Mock
    private TypeOfTrainingRepository typeOfTrainingRepository;

    @InjectMocks
    private TypeOfTrainingDAOImpl typeOfTrainingDAO;


    @Test
    @DisplayName("Get By Type - Should get type by type from repository")
    void getByType_shouldGetTypeByTypeFromRepository() throws EmptyException {
        TypeOfTraining mockTypeOfTraining = new TypeOfTraining(TYPE);
        when(typeOfTrainingRepository.getBy(TYPE)).thenReturn(Optional.of(mockTypeOfTraining));

        TypeOfTraining result = typeOfTrainingDAO.getBy(TYPE);

        assertThat(result).isEqualTo(mockTypeOfTraining);
        verify(typeOfTrainingRepository, times(1)).getBy(eq(TYPE));
    }

    @Test
    @DisplayName("Get By Type - Should throw EmptyException when type not found")
    void getByType_shouldThrowEmptyExceptionWhenTypeNotFound() {
        when(typeOfTrainingRepository.getBy(TYPE)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EmptyException.class)
                .isThrownBy(() -> typeOfTrainingDAO.getBy(TYPE));
        verify(typeOfTrainingRepository, times(1)).getBy(eq(TYPE));
    }

    @Test
    @DisplayName("Get All Types - Should get all types from repository")
    void getAll_shouldGetAllTypesFromRepository() {
        List<TypeOfTraining> mockTypes = List.of(new TypeOfTraining("Type1"), new TypeOfTraining("Type2"));
        when(typeOfTrainingRepository.getAll()).thenReturn(mockTypes);

        List<TypeOfTraining> result = typeOfTrainingDAO.getAll();

        assertThat(result).isEqualTo(mockTypes);
        verify(typeOfTrainingRepository, times(2)).getAll();
    }

    @Test
    @DisplayName("Add Type - Should add type to repository")
    void addType_shouldAddTypeToRepository() {
        typeOfTrainingDAO.add(TYPE);

        verify(typeOfTrainingRepository, times(1)).add(any(TypeOfTraining.class));
    }


    @Test
    @DisplayName("Get By Index - Should throw EmptyException when index out of bounds")
    void getByIndex_shouldThrowEmptyExceptionWhenIndexOutOfBounds() {

        assertThatExceptionOfType(EmptyException.class)
                .isThrownBy(() -> typeOfTrainingDAO.getByIndex(INDEX));
        verify(typeOfTrainingRepository, times(1)).getAll();
    }
}
