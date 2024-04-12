package dev.makeev.training_diary_app.repository.impl;

import dev.makeev.training_diary_app.model.UserLogEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("LogsRepository Test")
@ExtendWith(MockitoExtension.class)
class LogsRepositoryImplTest {

    private static final String LOGIN_1 = "TestLogin1";
    private static final String LOGIN_2 = "TestLogin2";

    private LogsRepositoryImpl logsRepositoryImpl;

    @BeforeEach
    void setUp() {
        logsRepositoryImpl = new LogsRepositoryImpl();
    }

    @Test
    @DisplayName("Adding UserLogEvent - Should save UserLogEvent")
    void addUserLogEvent_shouldSaveUserLogEvent() {
        final UserLogEvent testUserLogEvent1 = mock(UserLogEvent.class);
        when(testUserLogEvent1.login()).thenReturn(LOGIN_1);

        final UserLogEvent testUserLogEvent2 = mock(UserLogEvent.class);
        when(testUserLogEvent2.login()).thenReturn(LOGIN_2);

        List<UserLogEvent> events = logsRepositoryImpl.getAll();
        int sizeBeforeAdd = events.size();
        logsRepositoryImpl.add(testUserLogEvent1);
        logsRepositoryImpl.add(testUserLogEvent2);
        int sizeAfterAdd = events.size();

        assertNotNull(events);
        assertThat(events).hasSize(sizeAfterAdd - sizeBeforeAdd);
        assertThat(events.contains(testUserLogEvent1)).isTrue();
        assertThat(events.contains(testUserLogEvent2)).isTrue();
        assertThat(events.get(0).login()).isEqualTo(LOGIN_1);
        assertThat(events.get(1).login()).isEqualTo(LOGIN_2);
        assertThat(events.get(1).login()).isNotEqualTo(LOGIN_1);
    }

    @Test
    @DisplayName("Get All UserLogEvents - Should get all UserLogEvents")
    void getAllUserLogEvents_shouldGetAllUserLogEvents() {
        final UserLogEvent testUserLogEvent1 = mock(UserLogEvent.class);
        when(testUserLogEvent1.login()).thenReturn(LOGIN_1);

        final UserLogEvent testUserLogEvent2 = mock(UserLogEvent.class);
        when(testUserLogEvent2.login()).thenReturn(LOGIN_2);

        logsRepositoryImpl.add(testUserLogEvent1);
        logsRepositoryImpl.add(testUserLogEvent2);
        List<UserLogEvent> events = logsRepositoryImpl.getAll();

        assertNotNull(events);
        assertThat(events).hasSize(2);
        assertThat(events.get(0).login()).isEqualTo(LOGIN_1);
        assertThat(events.get(1).login()).isEqualTo(LOGIN_2);
        assertThat(events.get(1).login()).isNotEqualTo(LOGIN_1);
        assertThat(events.contains(testUserLogEvent1)).isTrue();
        assertThat(events.contains(testUserLogEvent2)).isTrue();
    }
}