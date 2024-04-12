package dev.makeev.training_diary_app.dao.impl;

import dev.makeev.training_diary_app.model.UserLogEvent;
import dev.makeev.training_diary_app.repository.LogsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserLogEventDAO Test")
@ExtendWith(MockitoExtension.class)
class UserLogEventDAOTest {

    private static final LocalDate DATE = LocalDate.now();
    private static final String LOGIN = "TestUser";
    private static final String MESSAGE = "TestMessage";

    @Mock
    private LogsRepository logsRepository;

    @InjectMocks
    private UserLogEventDAOImpl userLogEventDAO;


    @Test
    @DisplayName("Add Log Event - Should add log event to repository")
    void addLogEvent_shouldAddLogEventToRepository() {
        userLogEventDAO.add(DATE, LOGIN, MESSAGE);

        Mockito.verify(logsRepository, Mockito.times(1)).add(Mockito.any(UserLogEvent.class));
    }

    @Test
    @DisplayName("Get All Log Events - Should get all log events from repository")
    void getAllLogEvents_shouldGetAllLogEventsFromRepository() {
        List<UserLogEvent> mockEvents = List.of(new UserLogEvent(LocalDate.now(), "User1", "Message1"),
                new UserLogEvent(LocalDate.now(), "User2", "Message2"));
        Mockito.when(logsRepository.getAll()).thenReturn(mockEvents);

        List<UserLogEvent> result = userLogEventDAO.getAll();

        assertThat(result).isEqualTo(mockEvents);
        Mockito.verify(logsRepository, Mockito.times(1)).getAll();
    }
}
