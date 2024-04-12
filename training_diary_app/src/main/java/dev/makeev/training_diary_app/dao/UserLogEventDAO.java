package dev.makeev.training_diary_app.dao;

import dev.makeev.training_diary_app.model.UserLogEvent;

import java.time.LocalDate;
import java.util.List;

public interface UserLogEventDAO {
    void add(LocalDate date, String login, String message);

    List<UserLogEvent> getAll();
}
