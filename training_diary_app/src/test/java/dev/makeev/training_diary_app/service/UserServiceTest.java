package dev.makeev.training_diary_app.service;

import dev.makeev.training_diary_app.dao.UserDAO;
import dev.makeev.training_diary_app.exceptions.UserNotFoundException;
import dev.makeev.training_diary_app.exceptions.VerificationException;
import dev.makeev.training_diary_app.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("UserService Test")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String LOGIN = "TestUser";
    private static final String PASSWORD = "TestPassword";

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Add User - Should add new user to DAO")
    void addUser_shouldAddUserToDAO() {
        userService.addUser(LOGIN, PASSWORD);
    }

    @Test
    @DisplayName("Exist By Login - Should check if user exists in DAO")
    void existByLogin_shouldCheckIfUserExistsInDAO() {
        when(userDAO.getBy(LOGIN)).thenReturn(Optional.of(new User(LOGIN, PASSWORD,false)));

        boolean exists = userService.existByLogin(LOGIN);

        assertThat(exists).isTrue();
        verify(userDAO, times(1)).getBy(eq(LOGIN));
    }

    @Test
    @DisplayName("Check Credentials - Should verify user credentials")
    void checkCredentials_shouldVerifyUserCredentials() {
        when(userDAO.getBy(LOGIN)).thenReturn(Optional.of(new User(LOGIN, PASSWORD, false)));

        assertThatExceptionOfType(VerificationException.class)
                .isThrownBy(() -> userService.checkCredentials(LOGIN, "WrongPassword"));
        assertThatCode(() -> userService.checkCredentials(LOGIN, PASSWORD))
                .doesNotThrowAnyException();
        verify(userDAO, times(2)).getBy(eq(LOGIN));
    }

    @Test
    @DisplayName("Get by login- Should throw UserNotFoundException if user does not exist")
    void getByLogin_shouldThrowUserNotFoundExceptionIfUserDoesNotExist() {
        when(userDAO.getBy(LOGIN)).thenReturn(Optional.empty());

        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.isAdmin(LOGIN));
    }

    @Test
    @DisplayName("Is Admin - Should return true if user is admin")
    void isAdmin_shouldReturnTrueIfUserIsAdmin() throws UserNotFoundException {
        when(userDAO.getBy(LOGIN)).thenReturn(Optional.of(new User(LOGIN, PASSWORD, true)));

        boolean result = userService.isAdmin(LOGIN);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Is Admin - Should return false if user is not admin")
    void isAdmin_shouldReturnFalseIfUserIsNotAdmin() throws UserNotFoundException {
        when(userDAO.getBy(LOGIN)).thenReturn(Optional.of(new User(LOGIN, PASSWORD, false)));

        boolean result = userService.isAdmin(LOGIN);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Get All Users - Should get all users from DAO")
    void getAll_shouldGetAllUsersFromDAO() {
        List<User> mockUsers = List.of(new User("User1", "Password1", false), new User("User2", "Password2", false));
        when(userDAO.getAll()).thenReturn(mockUsers);

        List<User> users = userService.getAll();

        assertThat(users).isEqualTo(mockUsers);
        verify(userDAO, times(1)).getAll();
    }
}
