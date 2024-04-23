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
import static org.mockito.Mockito.*;

@DisplayName("UserService Test")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String LOGIN = "TestUser";
    private static final String PASSWORD = "TestPassword";
    private static final User TEST_USER = mock(User.class);

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Add User - Should add new user to DAO")
    void addUser_shouldAddUserToDAO() {
        userService.addUser(LOGIN, PASSWORD);

        verify(userDAO, times(1)).add(LOGIN, PASSWORD);
    }

    @Test
    @DisplayName("Get All Users - Should get all users from DAO")
    void getAll_shouldGetAllUsersFromDAO() {
        List<User> mockUsers = List.of(TEST_USER, TEST_USER);
        when(userDAO.getAll()).thenReturn(mockUsers);

        List<User> users = userService.getAll();

        assertThat(users).isEqualTo(mockUsers);
        verify(userDAO, times(1)).getAll();
    }

    @Test
    @DisplayName("Exist By Login - Should check if user exists in DAO")
    void existByLogin_shouldCheckIfUserExistsInDAO() {
        when(userDAO.getByLogin(LOGIN)).thenReturn(Optional.of(TEST_USER));

        boolean exists = userService.existByLogin(LOGIN);

        assertThat(exists).isTrue();
        verify(userDAO, times(1)).getByLogin(eq(LOGIN));
    }

    @Test
    @DisplayName("Check Credentials - Should verify user credentials")
    void checkCredentials_shouldVerifyUserCredentials() {
        when(TEST_USER.login()).thenReturn(LOGIN);
        when(TEST_USER.password()).thenReturn(PASSWORD);
        when(userDAO.getByLogin(LOGIN)).thenReturn(Optional.of(TEST_USER));

        assertThatCode(() -> userService.checkCredentials(LOGIN, PASSWORD))
                .doesNotThrowAnyException();
        verify(userDAO, times(1)).getByLogin(eq(LOGIN));
    }

    @Test
    @DisplayName("Check Credentials - Should throw VerificationException")
    void checkCredentials_shouldVerifyUserCredentials_shouldThrowVerificationException() {
        when(TEST_USER.login()).thenReturn(LOGIN);
        when(TEST_USER.password()).thenReturn(PASSWORD);
        when(userDAO.getByLogin(LOGIN)).thenReturn(Optional.of(TEST_USER));

        assertThatExceptionOfType(VerificationException.class).isThrownBy(
                () -> userService.checkCredentials(LOGIN, "Wrong password"));

        verify(userDAO, times(1)).getByLogin(eq(LOGIN));
    }

    @Test
    @DisplayName("Is Admin - Should return true if user is admin")
    void isAdmin_shouldReturnTrueIfUserIsAdmin() throws UserNotFoundException {
        when(userDAO.getByLogin(LOGIN)).thenReturn(Optional.of(TEST_USER));
        when(TEST_USER.admin()).thenReturn(true);

        boolean result = userService.isAdmin(LOGIN);

        assertThat(result).isTrue();
        verify(userDAO, times(1)).getByLogin(eq(LOGIN));
    }

    @Test
    @DisplayName("Is Admin - Should return false if user is not admin")
    void isAdmin_shouldReturnFalseIfUserIsNotAdmin() throws UserNotFoundException {
        when(userDAO.getByLogin(LOGIN)).thenReturn(Optional.of(new User(LOGIN, PASSWORD, false)));

        boolean result = userService.isAdmin(LOGIN);

        assertThat(result).isFalse();
        verify(userDAO, times(1)).getByLogin(eq(LOGIN));
    }
}
