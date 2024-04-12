package dev.makeev.training_diary_app.exceptions;

/**
 * {@code TrainingOnDateAlreadyExistsException} is an exception class that represents
 * a situation where a training session already exists for a given date.
 *
 * <p>This exception is thrown to indicate that a training session cannot be added
 * for a specific date because there is already a training session recorded for that day.
 */
public class TrainingOnDateAlreadyExistsException extends Exception {

    /**
     * Overrides the {@code getMessage} method to provide a custom error message
     * for the training on date already exists exception.
     *
     * @return A string representing the error message for there was already a training session that day.
     */
    @Override
    public String getMessage() {
        return "There was already a training session that day.";
    }
}
