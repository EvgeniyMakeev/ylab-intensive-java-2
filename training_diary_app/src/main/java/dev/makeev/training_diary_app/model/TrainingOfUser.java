package dev.makeev.training_diary_app.model;

import java.util.Map;

public record TrainingOfUser(
        String login,
        String typeOfTraining,
        Training training,
        Map<String, Double> additionalInformation) {
}
