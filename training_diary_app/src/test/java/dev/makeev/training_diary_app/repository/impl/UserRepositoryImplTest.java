package dev.makeev.training_diary_app.repository.impl;

import dev.makeev.training_diary_app.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("UserRepository Test")
@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    private final static String TEST_LOGIN = "TestLogin";
    private final static String WRONG_TEST_LOGIN = "WrongTestLogin";

    private UserRepositoryImpl userRepositoryImpl;

    @BeforeEach
    void setUp() {
        userRepositoryImpl = new UserRepositoryImpl();
    }

    @Test
    @DisplayName("Adding new User - Should save new user")
    void addNewUser_shouldSaveNewUser() {
        final User testUser = mock(User.class);
        when(testUser.login()).thenReturn(TEST_LOGIN);

        List<User> users = userRepositoryImpl.getAll();
        int sizeBeforeAdd = users.size();
        userRepositoryImpl.add(testUser);
        int sizeAfterAdd = users.size();

        assertNotNull(users);
        Assertions.assertThat(users).hasSize(sizeAfterAdd - sizeBeforeAdd);
        Assertions.assertThat(users).hasSize(3);
        Assertions.assertThat(users.contains(testUser)).isTrue();
        Assertions.assertThat(users.get(2).login()).isEqualTo(TEST_LOGIN);
    }

    @Test
    @DisplayName("Get User by login - Success")
    void getUserByLogin_shouldGetUser_whenExists() {
        final User testUser = mock(User.class);
        when(testUser.login()).thenReturn(TEST_LOGIN);

        userRepositoryImpl.add(testUser);
        final User actual = userRepositoryImpl.getBy(TEST_LOGIN);

        assertNotNull(actual);
        assertThat(actual).isEqualTo(testUser);
        assertThat(userRepositoryImpl.getBy(TEST_LOGIN).login()).isEqualTo(TEST_LOGIN);
        assertThat(userRepositoryImpl.getBy(WRONG_TEST_LOGIN)).isNull();
        assertThat(userRepositoryImpl.getBy(TEST_LOGIN).login()).isNotEqualTo(WRONG_TEST_LOGIN);
    }

    @Test
    @DisplayName("Getting all Users - Should get all Users")
    void getAllUsers_shouldGetAllUsers() {
        final User testUser = mock(User.class);
        when(testUser.login()).thenReturn(TEST_LOGIN);

        List<User> users = userRepositoryImpl.getAll();
        int sizeBeforeAdd = users.size();
        userRepositoryImpl.add(testUser);
        int sizeAfterAdd = users.size();

        assertNotNull(users);
        Assertions.assertThat(users).hasSize(sizeAfterAdd - sizeBeforeAdd);
        Assertions.assertThat(users.contains(testUser)).isTrue();
        Assertions.assertThat(users.get(2).login()).isEqualTo(TEST_LOGIN);
    }
}