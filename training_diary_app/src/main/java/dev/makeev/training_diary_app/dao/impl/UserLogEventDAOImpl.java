package dev.makeev.training_diary_app.dao.impl;

import dev.makeev.training_diary_app.dao.UserLogEventDAO;
import dev.makeev.training_diary_app.model.UserLogEvent;
import dev.makeev.training_diary_app.repository.LogsRepository;

import java.time.LocalDate;
import java.util.List;

public class UserLogEventDAOImpl implements UserLogEventDAO {

    private final LogsRepository repository;

    public UserLogEventDAOImpl(LogsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void add(LocalDate date, String login, String message) {
        repository.add(new UserLogEvent(date,login, message));
    }

    @Override
    public List<UserLogEvent> getAll() {
        return repository.getAll();
    }
}
