package dev.makeev.training_diary_app.exceptions;

public class NotAdminException extends Exception {

    /**
     * Overrides the {@code getMessage} method to provide a custom error message for the exception.
     *EmptyException
     * @return A string representing the error message for the empty exception.
     */
    @Override
    public String getMessage() {
        return "You do not have administrator rights.";
    }
}