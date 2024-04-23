package dev.makeev.training_diary_app.exceptions;

public class DaoException extends RuntimeException {
    public DaoException(Throwable e) {
        super(e);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
