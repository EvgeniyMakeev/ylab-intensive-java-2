package dev.makeev.training_diary_app.repository;

import dev.makeev.training_diary_app.model.UserLogEvent;

import java.util.ArrayList;
import java.util.List;

public class LogsRepository {
    /**
     * List to store user events.
     */
    private final List<UserLogEvent> eventList = new ArrayList<>();

    public void add(UserLogEvent userLogEvent) {
        eventList.add(userLogEvent);
    }

    public List<UserLogEvent> getAll() {
        return eventList;
    }
}
