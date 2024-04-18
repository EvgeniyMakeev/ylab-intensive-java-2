package dev.makeev.training_diary_app.model;

import java.time.LocalDate;

public record Training(
        long id,
        long typeOfTrainingId,
        LocalDate date,
        Double duration,
        Double caloriesBurned) {
}
