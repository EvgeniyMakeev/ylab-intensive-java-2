package dev.makeev.training_diary_app.dao;

import dev.makeev.training_diary_app.model.UserLogEvent;
import dev.makeev.training_diary_app.repository.LogsRepository;

import java.time.LocalDate;
import java.util.List;

public class UserLogEventDAO {

    private final LogsRepository repository = new LogsRepository();


    public void add(LocalDate date, String login, String message) {
        repository.add(new UserLogEvent(date,login, message));
    }

    public List<UserLogEvent> getAll() {
        return repository.getAll();
    }
}
