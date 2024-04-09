package dev.makeev.training_diary_app.exceptions;

public class TrainingNotFoundException extends Exception {

    @Override
    public String getMessage() {
        return "Training not found.";
    }
}
