package dev.makeev.training_diary_app.service;

import dev.makeev.training_diary_app.dao.UserLogEventDAO;
import dev.makeev.training_diary_app.model.UserLogEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("AdminService Test")
@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    private static final String LOGIN = "TestUser";
    private static final String MESSAGE = "TestMessage";

    @Mock
    private UserLogEventDAO userLogEventDAO;

    @InjectMocks
    private AdminService adminService;


    @Test
    @DisplayName("Add Event - Should add event to DAO")
    void addEvent_shouldAddEventToDAO() {
        adminService.addEvent(LOGIN, MESSAGE);

        verify(userLogEventDAO,times(1)).add(LocalDate.now(), LOGIN, MESSAGE);
    }

    @Test
    @DisplayName("Get All Events - Should get all events from DAO")
    void getAllEvents_shouldGetAllEventsFromDAO() {
        List<UserLogEvent> mockEvents = List.of(new UserLogEvent(LocalDate.now(), "User1", "Message1"),
                new UserLogEvent(LocalDate.now(), "User2", "Message2"));
        when(userLogEventDAO.getAll()).thenReturn(mockEvents);

        List<UserLogEvent> result = adminService.getAllEvents();

        assertThat(result).isEqualTo(mockEvents);
        verify(userLogEventDAO, times(1)).getAll();
    }

    @Test
    @DisplayName("Get All Events For User - Should get all events for specific user from DAO")
    void getAllEventsForUser_shouldGetAllEventsForUserFromDAO() {
        List<UserLogEvent> mockEvents = List.of(new UserLogEvent(LocalDate.now(), LOGIN, "Message1"),
                new UserLogEvent(LocalDate.now(), "AnotherUser", "Message2"));
        when(userLogEventDAO.getAll()).thenReturn(mockEvents);

        List<UserLogEvent> result = adminService.getAllEventsForUser(LOGIN);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).login()).isEqualTo(LOGIN);
        verify(userLogEventDAO, times(1)).getAll();
    }
}
