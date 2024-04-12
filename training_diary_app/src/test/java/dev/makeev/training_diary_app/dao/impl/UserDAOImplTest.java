package dev.makeev.training_diary_app.dao.impl;

import dev.makeev.training_diary_app.model.User;
import dev.makeev.training_diary_app.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("UserDAOImpl Test")
@ExtendWith(MockitoExtension.class)
class UserDAOImplTest {

    private static final String LOGIN = "TestUser";
    private static final String PASSWORD = "TestPassword";

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserDAOImpl userDAO;

    @Test
    @DisplayName("Add User - Should add new user to repository")
    void add_shouldAddUserToRepository() {
        userDAO.add(LOGIN, PASSWORD);

        verify(repository, times(1)).add(new User(LOGIN, PASSWORD, false));
    }

    @Test
    @DisplayName("Get By Login - Should return user from repository")
    void getBy_shouldReturnUserFromRepository() {
        User user = new User(LOGIN, PASSWORD, false);
        when(repository.getBy(LOGIN)).thenReturn(user);

        Optional<User> result = userDAO.getBy(LOGIN);

        assertTrue(result.isPresent());
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    @DisplayName("Get By Login - Should return empty optional if user does not exist")
    void getBy_shouldReturnEmptyOptionalIfUserDoesNotExist() {
        when(repository.getBy(LOGIN)).thenReturn(null);

        Optional<User> result = userDAO.getBy(LOGIN);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Get All Users - Should return all users from repository")
    void getAll_shouldReturnAllUsersFromRepository() {
        List<User> mockUsers = List.of(new User("User1", "Password1", false), new User("User2", "Password2", false));
        when(repository.getAll()).thenReturn(mockUsers);

        List<User> users = userDAO.getAll();

        assertThat(users).isEqualTo(mockUsers);
    }
}