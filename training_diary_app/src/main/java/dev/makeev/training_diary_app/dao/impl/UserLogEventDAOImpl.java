package dev.makeev.training_diary_app.dao.impl;

import dev.makeev.training_diary_app.dao.UserLogEventDAO;
import dev.makeev.training_diary_app.model.UserLogEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserLogEventDAOImpl implements UserLogEventDAO {

    /**
     * List to store user events.
     */
    private final List<UserLogEvent> eventList = new ArrayList<>();

    @Override
    public void add(LocalDate date, String login, String message) {
        UserLogEvent userLogEvent = new UserLogEvent(date,login, message);
        eventList.add(userLogEvent);
    }

    @Override
    public List<UserLogEvent> getAll() {
        return eventList;
    }
}
