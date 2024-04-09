package dev.makeev.training_diary_app.dao;

import dev.makeev.training_diary_app.exceptions.EmptyException;
import dev.makeev.training_diary_app.model.TypeOfTraining;
import dev.makeev.training_diary_app.repository.TypeOfTrainingRepository;

import java.util.List;
import java.util.Optional;

public class TypeOfTrainingDAO {

    private final TypeOfTrainingRepository repository = new TypeOfTrainingRepository();
    private int sizeOfList = repository.getAll().size();

    public TypeOfTraining getBy(String type) throws EmptyException {
        Optional<TypeOfTraining> typeOfTraining = repository.getBy(type);
        if (typeOfTraining.isPresent()) {
            return typeOfTraining.get();
        } else {
            throw new EmptyException();
        }
    }

    public List<TypeOfTraining> getAll() {
        return repository.getAll();
    }

    public void add(String type) {
        repository.add(new TypeOfTraining(type));
    }

    public Optional<TypeOfTraining> getByIndex(int index) throws EmptyException {
        index -= 1;
        if (index < sizeOfList) {
            return Optional.ofNullable(repository.getAll().get(index));
        } else {
            throw new EmptyException();
        }
    }

    public int getSizeOfList() {
        return sizeOfList;
    }
}
