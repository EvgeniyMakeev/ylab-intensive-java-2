package dev.makeev.training_diary_app.utils;

import dev.makeev.training_diary_app.exceptions.DaoException;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.SQLException;

/**
 * The {@code InitDB} class provides a method to initialize the database schema
 * and apply migrations using Liquibase.
 */
public class InitDB {

    private final ConnectionManager connectionManager;

    /**
     * Constructs a new {@code InitDB} object with the specified {@code ConnectionManager}.
     *
     * @param connectionManager The {@code ConnectionManager} to use for database connections.
     */
    public InitDB(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Initializes the database schema and applies migrations using Liquibase.
     * If the "non_public" schema does not exist, it creates it.
     */
    public void initDB() {
        try (var connection = connectionManager.open()) {
            String sql = "CREATE SCHEMA IF NOT EXISTS non_public";
            var statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DaoException(e);
        }

        try (var connection = connectionManager.open()) {
            var database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setDefaultSchemaName("non_public");
            var liquibase = new Liquibase("db/changelog/changelog.xml",
                    new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Migration is completed successfully");
        } catch (LiquibaseException | SQLException e) {
            System.out.println("SQL Exception in migration " + e.getMessage());
        }
    }
}
