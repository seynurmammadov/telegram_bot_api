package az.code.telegram_bot_api.schedules;

import az.code.telegram_bot_api.SPRING_TEST_DATA;
import az.code.telegram_bot_api.models.Request;
import az.code.telegram_bot_api.models.UserRequest;
import az.code.telegram_bot_api.models.enums.RequestStatus;
import az.code.telegram_bot_api.repositories.RequestRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Collections;

import static org.mockito.Mockito.*;

class RequestScheduleTest {
    SPRING_TEST_DATA data = new SPRING_TEST_DATA();

    @Test
    @DisplayName("Request Schedule - expired Requests - Valid")
    void expiredRequests() throws CloneNotSupportedException {
        RequestSchedule rs = new RequestSchedule(mock(RequestRepository.class), mock(RabbitTemplate.class));
        Request request = data.generateRequest();
        request.setUserRequests(Collections.singletonList(data.generateUserRequest(request, null, RequestStatus.NEW_REQUEST, false)));
        when(rs.requestRepository.getAllActive()).thenReturn(Collections.singletonList(request));
        rs.expiredRequests();
        Request expected = getExpected(request);
        verify(rs.requestRepository).save(expected);
    }

    private Request getExpected(Request request) throws CloneNotSupportedException {
        Request expected = (Request) request.clone();
        expected.setActive(false);
        UserRequest expectedUR = request.getUserRequests().get(0);
        expectedUR.setRequestStatus(RequestStatus.EXPIRED);
        expectedUR.setArchived(false);
        expected.setUserRequests(Collections.singletonList(expectedUR));
        return expected;
    }

}