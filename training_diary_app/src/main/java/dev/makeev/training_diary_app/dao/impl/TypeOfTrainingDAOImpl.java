package dev.makeev.training_diary_app.dao.impl;

import dev.makeev.training_diary_app.dao.TypeOfTrainingDAO;
import dev.makeev.training_diary_app.exceptions.EmptyException;
import dev.makeev.training_diary_app.model.TypeOfTraining;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TypeOfTrainingDAOImpl implements TypeOfTrainingDAO {

    private final List<TypeOfTraining> listOfTypesOfTraining;
    private int sizeOfList;

    {
        listOfTypesOfTraining = new ArrayList<>();
        listOfTypesOfTraining.add(new TypeOfTraining("Cardio training"));
        listOfTypesOfTraining.add(new TypeOfTraining("Power training"));
        listOfTypesOfTraining.add(new TypeOfTraining("Yoga"));
        sizeOfList = listOfTypesOfTraining.size();
    }

    @Override
    public void add(String type) {
        TypeOfTraining typeOfTraining  = new TypeOfTraining(type);
        listOfTypesOfTraining.add(typeOfTraining);
        sizeOfList = listOfTypesOfTraining.size();
    }

    @Override
    public TypeOfTraining getByType(String type) throws EmptyException {
        Optional<TypeOfTraining> typeOfTraining = listOfTypesOfTraining.stream()
                .filter(t -> t.type().equalsIgnoreCase(type))
                .findFirst();

        if (typeOfTraining.isPresent()) {
            return typeOfTraining.get();
        } else {
            throw new EmptyException();
        }
    }

    @Override
    public List<TypeOfTraining> getAll() {
        return listOfTypesOfTraining;
    }

    @Override
    public Optional<TypeOfTraining> getByIndex(int index) throws EmptyException {
        index -= 1;
        if (index < sizeOfList) {
            return Optional.ofNullable(listOfTypesOfTraining.get(index));
        } else {
            throw new EmptyException();
        }
    }
}
