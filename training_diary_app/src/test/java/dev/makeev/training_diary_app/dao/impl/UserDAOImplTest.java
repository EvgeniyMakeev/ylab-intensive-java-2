package dev.makeev.training_diary_app.dao.impl;

import dev.makeev.training_diary_app.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserDAOImpl Test")
@ExtendWith(MockitoExtension.class)
class UserDAOImplTest {

    private final static String LOGIN = "TestLogin";
    private static final String PASSWORD = "TestPassword";
    private final static String WRONG_LOGIN = "WrongTestLogin";

    private UserDAOImpl userDAO;

    @BeforeEach
    public void setUp() {
        userDAO = new UserDAOImpl();
    }

    @Test
    @DisplayName("Add User - Should add new user")
    void add_shouldAddUser() {
        List<User> users = userDAO.getAll();
        int sizeBeforeAdd = users.size();
        userDAO.add(LOGIN, PASSWORD);
        List<User> usersAfterAdd = userDAO.getAll();

        Assertions.assertThat(usersAfterAdd).hasSize(sizeBeforeAdd + 1);
        assertThat(userDAO.getByLogin(LOGIN)).isNotNull();
    }

    @Test
    @DisplayName("Get User by login - Success")
    void getBy_shouldGetUser_whenExists() {
        userDAO.add(LOGIN, PASSWORD);

        Optional<User> user = userDAO.getByLogin(LOGIN);

        assertThat(user).isPresent();
        assertThat(user.get().password()).isEqualTo(PASSWORD);
    }

    @Test
    @DisplayName("Get By Login - Should return empty optional if user does not exist")
    void getBy_shouldReturnEmptyOptionalIfUserDoesNotExist() {
        userDAO.add(LOGIN, PASSWORD);
        Optional<User> result = userDAO.getByLogin(WRONG_LOGIN);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Get All Users - Should return all users")
    void getAll_shouldReturnAllUsers() {
        userDAO.add(LOGIN, PASSWORD);
        List<User> users = userDAO.getAll();

        assertThat(users).isNotNull();
        assertThat(userDAO.getByLogin(LOGIN)).isNotNull();
    }
}