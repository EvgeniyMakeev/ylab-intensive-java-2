package dev.makeev.training_diary_app.repository;

import dev.makeev.training_diary_app.model.TypeOfTraining;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TypeOfTrainingRepository {

    private final List<TypeOfTraining> listOfTypesOfTraining = new ArrayList<>();

    {
        listOfTypesOfTraining.add(new TypeOfTraining("Cardio training"));
        listOfTypesOfTraining.add(new TypeOfTraining("Power training"));
        listOfTypesOfTraining.add(new TypeOfTraining("Yoga"));
    }

    public void add(TypeOfTraining typeOfTraining) {
        listOfTypesOfTraining.add(typeOfTraining);
    }

    public Optional<TypeOfTraining> getBy(String typeOfTraining) {
        return listOfTypesOfTraining.stream()
                .filter(t -> t.type().equalsIgnoreCase(typeOfTraining))
                .findFirst();
    }

    public List<TypeOfTraining> getAll() {
        return listOfTypesOfTraining;
    }

}
