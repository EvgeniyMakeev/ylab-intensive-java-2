package dev.makeev.training_diary_app.repository;

import dev.makeev.training_diary_app.model.UserLogEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LogsRepository Test")
class LogsRepositoryTest {

    private LogsRepository logsRepository;

    @BeforeEach
    void setUp() {
        logsRepository = new LogsRepository();
    }

    @Test
    @DisplayName("Adding UserLogEvent - Success")
    void testAddUserLogEvent_Success() {
        LocalDate testDate = LocalDate.of(2222, 10, 10);
        String testLogin = "Test_login";
        String testMessage = "Test message";
        UserLogEvent testUserLogEvent = new UserLogEvent(testDate,testLogin, testMessage);

        logsRepository.add(testUserLogEvent);

        List<UserLogEvent> events = logsRepository.getAll();

        assertThat(events).containsExactly(testUserLogEvent);
    }

    @Test
    @DisplayName("Get All UserLogEvents - Success")
    void testGetAllUserLogEvents_Success() {
        LocalDate testDate1 = LocalDate.of(1111, 11, 11);
        String testLogin1 = "Test_login_1";
        String testMessage1 = "Test message_1";
        UserLogEvent testEvent1 = new UserLogEvent(testDate1,testLogin1, testMessage1);

        LocalDate testDate2 = LocalDate.of(2222, 2, 22);
        String testLogin2 = "Test_login_2";
        String testMessage2 = "Test message_2";
        UserLogEvent testEvent2 = new UserLogEvent(testDate2,testLogin2, testMessage2);

        logsRepository.add(testEvent1);
        logsRepository.add(testEvent2);

        List<UserLogEvent> events = logsRepository.getAll();

        assertThat(events).containsExactly(testEvent1, testEvent2);
    }
}