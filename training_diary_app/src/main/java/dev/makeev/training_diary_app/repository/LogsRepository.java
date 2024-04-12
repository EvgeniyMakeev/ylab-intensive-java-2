package dev.makeev.training_diary_app.repository;

import dev.makeev.training_diary_app.model.UserLogEvent;

import java.util.List;

public interface LogsRepository {
    void add(UserLogEvent userLogEvent);

    List<UserLogEvent> getAll();
}
