package dev.makeev.training_diary_app.model;

import java.time.LocalDate;

public record Training(
        TypeOfTraining type,
        LocalDate date,
        double duration,
        double caloriesBurned) {
}

//Map<String, String> additionalInformation