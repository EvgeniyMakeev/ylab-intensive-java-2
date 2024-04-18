package dev.makeev.training_diary_app.utils;

import java.sql.Connection;

public interface ConnectionManager {
    Connection open();
}
