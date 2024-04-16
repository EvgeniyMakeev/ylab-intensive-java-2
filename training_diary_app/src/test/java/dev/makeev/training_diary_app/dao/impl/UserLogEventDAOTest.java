package dev.makeev.training_diary_app.dao.impl;

import dev.makeev.training_diary_app.model.UserLogEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserLogEventDAO Test")
class UserLogEventDAOTest {

    private static final LocalDate DATE = LocalDate.now();
    private static final String LOGIN = "TestUser";
    private static final String MESSAGE = "TestMessage";

    private UserLogEventDAOImpl userLogEventDAO;

    @BeforeEach
    public void setUp() {
        userLogEventDAO = new UserLogEventDAOImpl();
    }

    @Test
    @DisplayName("Add Log Event - Should add log event")
    void addLogEvent_shouldAddLogEvent() {
        List<UserLogEvent> events = userLogEventDAO.getAll();
        int sizeBeforeAdd = events.size();
        userLogEventDAO.add(DATE, LOGIN, MESSAGE);
        List<UserLogEvent> eventsAfterAdd = userLogEventDAO.getAll();
        int sizeAfterAdd = eventsAfterAdd.size();

        assertThat(eventsAfterAdd).isNotNull();
        assertThat(eventsAfterAdd).isNotEmpty();
        assertThat(eventsAfterAdd).hasSize(sizeAfterAdd - sizeBeforeAdd);
        assertThat(eventsAfterAdd.get(0).date()).isEqualTo(DATE);
        assertThat(eventsAfterAdd.get(0).login()).isEqualTo(LOGIN);
        assertThat(eventsAfterAdd.get(0).message()).isEqualTo(MESSAGE);
    }

    @Test
    @DisplayName("Get All Log Events - Should get all log events from repository")
    void getAllLogEvents_shouldGetAllLogEventsFromRepository() {
        List<UserLogEvent> events = userLogEventDAO.getAll();
        int sizeBeforeAdd = events.size();
        userLogEventDAO.add(DATE, LOGIN, MESSAGE);
        userLogEventDAO.add(LocalDate.now(), "TestLogin2", "SomeMessage");
        List<UserLogEvent> eventsAfterAdd = userLogEventDAO.getAll();
        int sizeAfterAdd = eventsAfterAdd.size();

        assertThat(eventsAfterAdd).isNotNull();
        assertThat(eventsAfterAdd).isNotEmpty();
        assertThat(eventsAfterAdd).hasSize(sizeAfterAdd - sizeBeforeAdd);
    }
}
