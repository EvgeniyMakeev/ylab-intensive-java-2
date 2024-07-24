package dev.makeev.training_diary_app.utils;

import java.sql.Connection;

/**
 * The {@code ConnectionManager} interface provides a method for opening a database connection.
 */
public interface ConnectionManager {

    /**
     * Opens a new database connection.
     *
     * @return A {@code Connection} object representing the new database connection.
     */
    Connection open();
}
