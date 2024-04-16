package dev.makeev.training_diary_app.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public record Training(
        TypeOfTraining type,
        LocalDate date,
        Double duration,
        Double caloriesBurned,
        Map<String, Double> additionalInfo) {

    public Training(TypeOfTraining type, LocalDate date, Double duration, Double caloriesBurned) {
        this(type, date, duration, caloriesBurned, new HashMap<>());
    }
}
