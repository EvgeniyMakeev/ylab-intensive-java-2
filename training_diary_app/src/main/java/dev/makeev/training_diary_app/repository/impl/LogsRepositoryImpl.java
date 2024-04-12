package dev.makeev.training_diary_app.repository.impl;

import dev.makeev.training_diary_app.model.UserLogEvent;
import dev.makeev.training_diary_app.repository.LogsRepository;

import java.util.ArrayList;
import java.util.List;

public class LogsRepositoryImpl implements LogsRepository {
    /**
     * List to store user events.
     */
    private final List<UserLogEvent> eventList = new ArrayList<>();

    @Override
    public void add(UserLogEvent userLogEvent) {
        eventList.add(userLogEvent);
    }

    @Override
    public List<UserLogEvent> getAll() {
        return eventList;
    }
}
