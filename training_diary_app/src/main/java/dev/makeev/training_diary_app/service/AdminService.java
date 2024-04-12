package dev.makeev.training_diary_app.service;

import dev.makeev.training_diary_app.dao.UserLogEventDAO;
import dev.makeev.training_diary_app.model.UserLogEvent;

import java.time.LocalDate;
import java.util.List;

/**
 * Manages admin-related events.
 */
public class AdminService {

    private final UserLogEventDAO userLogEventDAO;

    public AdminService(UserLogEventDAO userLogEventDAO) {
        this.userLogEventDAO = userLogEventDAO;
    }

    /**
     * Adds an event for a specific user with a corresponding message.
     *
     * @param login The user of login for whom the event is added.
     * @param message The message describing the event.
     */
    public void addEvent(String login, String message) {
        LocalDate date = LocalDate.now();
        userLogEventDAO.add(date, login, message);
    }

    /**
     * Retrieves the submission history of all events.
     *
     * @return A formatted string representing the submission history of all events.
     */
    public List<UserLogEvent> getAllEvents() {
        return userLogEventDAO.getAll();
    }

    /**
     * Retrieves the submission history of events for a specific user.
     *
     * @param login The login of the user.
     * @return A formatted string representing the submission history of events for the user.
     */
    public List<UserLogEvent> getAllEventsForUser(String login) {
       return userLogEventDAO.getAll()
               .stream()
               .filter(e -> e.login().equals(login))
               .toList();
    }

}
