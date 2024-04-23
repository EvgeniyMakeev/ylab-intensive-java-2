package dev.makeev.training_diary_app.exceptions;

/**
 * {@code TrainingNotFoundException} is an exception class that represents
 * a situation where a training entity is not found.
 */
public class TrainingNotFoundException extends Exception {

    @Override
    public String getMessage() {
        return "Training not found.";
    }
}
