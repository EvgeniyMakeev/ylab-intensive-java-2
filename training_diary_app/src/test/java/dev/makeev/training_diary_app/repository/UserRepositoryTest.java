package dev.makeev.training_diary_app.repository;

import dev.makeev.training_diary_app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("UserRepository Test")
class UserRepositoryTest {

    private UserRepository userRepository;
    private String testLogin;
    private String testPassword;
    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
        testLogin = "TestLogin";
        testPassword = "TestPassword";
        testUser = new User(testLogin, testPassword, false);
    }

    @Test
    @DisplayName("Adding new User - Success")
    void addNewUser_Success() {
        userRepository.add(testUser);

        assertThat(userRepository.getAll().contains(testUser)).isTrue();
        assertThat(userRepository.getBy("Wrong login")).isNull();
        assertThat(userRepository.getBy(testLogin)).isEqualTo(testUser);
        assertThat(userRepository.getBy(testLogin).login()).isEqualTo(testLogin);
        assertThat(userRepository.getBy(testLogin).password()).isEqualTo(testPassword);
    }

    @Test
    @DisplayName("Get User by login - Success")
    void getUserByLogin_Success() {
        userRepository.add(testUser);

        assertThat(userRepository.getBy(testLogin)).isEqualTo(testUser);
        assertThat(userRepository.getBy("Wrong login")).isNull();
        assertThat(userRepository.getBy(testLogin).login()).isEqualTo(testLogin);
        assertThat(userRepository.getBy(testLogin).password()).isEqualTo(testPassword);
    }

    @Test
    @DisplayName("Getting all Users - Success")
    void getAllUsers_Success() {
        List<User> testList = userRepository.getAll();
        int sizeBeforeAdd = testList.size();

        userRepository.add(testUser);

        assertThat(userRepository.getAll().size()).isEqualTo(sizeBeforeAdd + 1);
        assertThat(userRepository.getAll().contains(testUser)).isTrue();
    }
}