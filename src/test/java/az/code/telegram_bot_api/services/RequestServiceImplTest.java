package az.code.telegram_bot_api.services;

import az.code.telegram_bot_api.SPRING_TEST_DATA;
import az.code.telegram_bot_api.models.Request;
import az.code.telegram_bot_api.models.User;
import az.code.telegram_bot_api.models.UserRequest;
import az.code.telegram_bot_api.models.enums.RequestStatus;
import az.code.telegram_bot_api.services.interfaces.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collections;

import static az.code.telegram_bot_api.utils.BaseUtils.getPageable;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RequestServiceImplTest {

    @Autowired
    RequestServiceImpl requestService;
    @Autowired
    UserService userService;
    SPRING_TEST_DATA data = new SPRING_TEST_DATA();


    @Test
    @DisplayName("Request Service - get all requests Valid")
    void getAll() {
        User user = getUser(false);
        assertEquals(new ArrayList<>(user.getUserRequests()),
                requestService.getAll(data.generateUserDTO(user),
                        getPageable(0, 5, "requestStatus")));
    }

    @Test
    @DisplayName("Request Service - get all request filtered Valid")
    void getAllFiltered() {
        User user = getUser(false);
        assertEquals(new ArrayList<>(user.getUserRequests()),
                requestService.getAll(
                        data.generateUserDTO(user),
                        getPageable(0, 5, "requestStatus"),
                        RequestStatus.NEW_REQUEST.getVal())
        );
    }


    @Test
    @DisplayName("Request Service - get all archived requests Valid")
    void getAllArchived() {
        User user = getUser();
        UserRequest ur = getUserRequest(user, true);
        assertEquals(Collections.singletonList(ur),
                requestService.getAllArchived(
                        data.generateUserDTO(user),
                        getPageable(0, 5, "requestStatus")
                )
        );
    }

    @Test
    @DisplayName("Request Service - archive request Valid")
    void archiveRequest() {
        User user = getUser();
        UserRequest ur = getUserRequest(user, false);
        assertEquals(HttpStatus.OK, requestService.archiveRequest(data.generateUserDTO(user), ur.getId()));
        ur.setArchived(true);
        assertEquals(Collections.singletonList(ur),
                requestService.getAllArchived(
                        data.generateUserDTO(user),
                        getPageable(0, 5, "requestStatus")
                )
        );
    }

    @Test
    @DisplayName("Request Service - unarchive request Valid")
    void unarchiveRequest() {
        User user = getUser();
        UserRequest ur = getUserRequest(user, true);
        assertEquals(HttpStatus.OK, requestService.unarchiveRequest(data.generateUserDTO(user), ur.getId()));
        ur.setArchived(false);
        assertEquals(Collections.singletonList(ur),
                requestService.getAll(
                        data.generateUserDTO(user),
                        getPageable(0, 5, "requestStatus")
                )
        );
    }

    @Test
    @DisplayName("Request Service - delete request Valid")
    void deleteArchived() {
        User user = getUser();
        UserRequest ur = getUserRequest(user, true);
        assertEquals(HttpStatus.OK, requestService.deleteArchived(data.generateUserDTO(user), ur.getId()));
        assertEquals(Collections.emptyList(),
                requestService.getAll(
                        data.generateUserDTO(user),
                        getPageable(0, 5, "requestStatus")
                )
        );
    }

    @Test
    @DisplayName("Request Service - get request for offer Valid")
    void getForOffer() {
        User user = getUser();
        UserRequest ur = getUserRequest(user, false);
        assertEquals(ur,
                requestService.getForOffer(
                        user.getUsername(),
                        ur.getId()
                )
        );
    }

    @Test
    @DisplayName("Request Service - get request for offer Valid")
    void getForOffer_test2() {
        User user = getUser();
        getUserRequest(user, false);
        assertFalse(getForOffer(user.getUsername()));
    }

    private boolean getForOffer(String username) {
        try {
            requestService.getForOffer(
                    username,
                    0L
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    @DisplayName("Request Service - get request for accepted offer Valid")
    void getForAccepted() {
        User user = getUser();
        UserRequest ur = getUserRequest(user, false);
        assertEquals(ur,
                requestService.getForAccepted(
                        user.getUsername(),
                        ur.getRequest().getUUID()
                )
        );
    }

    private User getUser(boolean archived) {
        User user = getUser();
        Request request = requestService.requestRepository.save(data.generateRequest());
        user.addRequest(data.generateUserRequest(request, user, RequestStatus.NEW_REQUEST, archived));
        userService.addRequestToUsers(request);
        return user;
    }

    private UserRequest getUserRequest(User user, boolean b) {
        Request request = requestService.requestRepository.save(data.generateRequest());
        UserRequest ur = data.generateUserRequest(request, user, RequestStatus.USER_CANCEL, b);
        ur = requestService.userReqRepo.save(ur);
        return ur;
    }

    private User getUser() {
        User user = data.generateUser();
        userService.save(user);
        return user;
    }

}