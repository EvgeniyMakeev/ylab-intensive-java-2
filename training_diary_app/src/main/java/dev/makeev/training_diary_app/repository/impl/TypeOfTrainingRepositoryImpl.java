package dev.makeev.training_diary_app.repository.impl;

import dev.makeev.training_diary_app.model.TypeOfTraining;
import dev.makeev.training_diary_app.repository.TypeOfTrainingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TypeOfTrainingRepositoryImpl implements TypeOfTrainingRepository {

    private final List<TypeOfTraining> listOfTypesOfTraining = new ArrayList<>();

    {
        listOfTypesOfTraining.add(new TypeOfTraining("Cardio training"));
        listOfTypesOfTraining.add(new TypeOfTraining("Power training"));
        listOfTypesOfTraining.add(new TypeOfTraining("Yoga"));
    }

    @Override
    public void add(TypeOfTraining typeOfTraining) {
        listOfTypesOfTraining.add(typeOfTraining);
    }

    @Override
    public Optional<TypeOfTraining> getBy(String typeOfTraining) {
        return listOfTypesOfTraining.stream()
                .filter(t -> t.type().equalsIgnoreCase(typeOfTraining))
                .findFirst();
    }

    @Override
    public List<TypeOfTraining> getAll() {
        return listOfTypesOfTraining;
    }

}
