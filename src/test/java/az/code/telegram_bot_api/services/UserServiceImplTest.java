package az.code.telegram_bot_api.services;

import az.code.telegram_bot_api.SPRING_TEST_DATA;
import az.code.telegram_bot_api.models.Request;
import az.code.telegram_bot_api.models.User;
import az.code.telegram_bot_api.models.enums.RequestStatus;
import az.code.telegram_bot_api.repositories.RequestRepository;
import az.code.telegram_bot_api.services.interfaces.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {
    @Autowired
    UserService userService;
    @Autowired
    RequestRepository requestRepository;
    SPRING_TEST_DATA data = new SPRING_TEST_DATA();

    @Test
    @DisplayName("User Service - find user by email - Valid")
    void findUserByEmail_test1() {
        User user = userService.save(data.generateUser());
        assertEquals(user, userService.findUserByEmail(user.getEmail()));
    }

    @Test
    @DisplayName("User Service - find user by email  - Valid")
    void findUserByEmail_test2() {
        userService.save(data.generateUser());
        assertFalse(findUserByEmail());
    }

    private boolean findUserByEmail() {
        try {
            userService.findUserByEmail("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Test
    @DisplayName("User Service - active And Save - Valid")
    void activeAndSave() {
        User user = userService.save(data.generateUser());
        User expected = user;
        expected.setActive(true);
        assertEquals(expected, userService.activeAndSave(user));
    }

    @Test
    @DisplayName("User Service - add Request To Users - Valid")
    void addRequestToUsers() {
        User user = data.generateUser();
        user.setActive(true);
        user = userService.save(user);
        User expectedUser = user;
        Request request = requestRepository.save(data.generateRequest());
        expectedUser.addRequest(data.generateUserRequest(request, user, RequestStatus.NEW_REQUEST,false));
        userService.addRequestToUsers(request);
        assertEquals(expectedUser,userService.findUserByEmail(user.getEmail()));
    }
}