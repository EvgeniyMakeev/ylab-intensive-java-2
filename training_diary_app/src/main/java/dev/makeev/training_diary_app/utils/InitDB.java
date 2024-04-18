package dev.makeev.training_diary_app.utils;

import dev.makeev.training_diary_app.exceptions.DaoException;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.SQLException;

public class InitDB {

    private final ConnectionManager connectionManager;

    public InitDB(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

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
