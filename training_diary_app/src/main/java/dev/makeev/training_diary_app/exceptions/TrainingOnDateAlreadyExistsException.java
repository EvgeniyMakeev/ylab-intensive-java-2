package dev.makeev.training_diary_app.exceptions;

public class TrainingOnDateAlreadyExistsException extends Exception {
    @Override
    public String getMessage() {
        return "There was already a training session that day.";
    }
}
