package dev.makeev.training_diary_app.dao.impl;

import dev.makeev.training_diary_app.dao.TypeOfTrainingDAO;
import dev.makeev.training_diary_app.exceptions.EmptyException;
import dev.makeev.training_diary_app.model.TypeOfTraining;
import dev.makeev.training_diary_app.repository.TypeOfTrainingRepository;

import java.util.List;
import java.util.Optional;

public class TypeOfTrainingDAOImpl implements TypeOfTrainingDAO {

    private final TypeOfTrainingRepository repository;
    private int sizeOfList;

    public TypeOfTrainingDAOImpl(TypeOfTrainingRepository repository) {
        this.repository = repository;
        this.sizeOfList = repository.getAll().size();
    }


    @Override
    public TypeOfTraining getBy(String type) throws EmptyException {
        Optional<TypeOfTraining> typeOfTraining = repository.getBy(type);
        if (typeOfTraining.isPresent()) {
            return typeOfTraining.get();
        } else {
            throw new EmptyException();
        }
    }

    @Override
    public List<TypeOfTraining> getAll() {
        return repository.getAll();
    }

    @Override
    public void add(String type) {
        repository.add(new TypeOfTraining(type));
        sizeOfList = getAll().size();
    }

    @Override
    public Optional<TypeOfTraining> getByIndex(int index) throws EmptyException {
        index -= 1;
        if (index < sizeOfList) {
            return Optional.ofNullable(repository.getAll().get(index));
        } else {
            throw new EmptyException();
        }
    }
}
