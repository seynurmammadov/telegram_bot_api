package az.code.telegram_bot_api.services;

import az.code.telegram_bot_api.SPRING_TEST_DATA;
import az.code.telegram_bot_api.models.*;
import az.code.telegram_bot_api.models.DTOs.RequestDTO;
import az.code.telegram_bot_api.models.DTOs.UserData;
import az.code.telegram_bot_api.models.enums.RequestStatus;
import az.code.telegram_bot_api.models.mapper.MapperModel;
import az.code.telegram_bot_api.repositories.AcceptedOfferRepository;
import az.code.telegram_bot_api.repositories.LanguageRepository;
import az.code.telegram_bot_api.repositories.RequestRepository;
import az.code.telegram_bot_api.utils.TimeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

class ListenerServiceImplTest {
    SPRING_TEST_DATA data = new SPRING_TEST_DATA();
    ListenerServiceImpl ls;

    @BeforeEach
    void setData() {
        ls = mockLS();
    }

    @Test
    @DisplayName("Listener Service - save Request - Valid")
    void saveRequest() throws CloneNotSupportedException {
        RequestDTO requestDTO = data.generateRequestDTO();
        UserData userData = data.generateUserData();

        when(ls.mapperModel.defaultMap(any(), any())).thenReturn(requestDTO);
        requestDTO.setUUID(userData.getUUID());
        Request request = data.toRequest(requestDTO);
        when(ls.mapperModel.requestDTOtoRequest(any(), any(), any())).thenReturn(request);

        ls.saveRequest(userData);
        Request expected = (Request) request.clone();
        verify(ls.requestRepository).save(expected);
    }

    @Test
    @DisplayName("Listener Service - cancel Request - Valid")
    void cancelRequest() throws CloneNotSupportedException {
        Request request = data.generateRequest();
        when(ls.requestRepository.findByUUID(anyString())).thenReturn(Optional.ofNullable(request));
        ls.cancelRequest("test");
        Request expected = (Request) request.clone();
        expected.setActive(false);
        verify(ls.requestRepository).save(expected);
    }

    @Test
    @DisplayName("Listener Service - accepted Offer - Valid")
    void acceptedOffer() throws CloneNotSupportedException {
        User user = data.generateUser();
        AcceptedOffer offer = data.generateAcceptedOffer(user);
        Request request = data.generateRequest();
        UserRequest ur = data.generateUserRequest(request, user, RequestStatus.NEW_REQUEST, false);

        when(ls.requestService.getForAccepted(anyString(), anyString())).thenReturn(ur);
        ls.acceptedOffer(offer);

        UserRequest expectedUR = (UserRequest) ur.clone();
        AcceptedOffer expectedOffer = getExpectedAcceptedOffer(offer, expectedUR);

        verify(ls.acceptedRepo).save(expectedOffer);
        verify(ls.requestService).save(expectedUR);
    }

    private AcceptedOffer getExpectedAcceptedOffer(AcceptedOffer offer, UserRequest expectedUR) throws CloneNotSupportedException {
        AcceptedOffer expectedOffer = (AcceptedOffer) offer.clone();
        expectedUR.setRequestStatus(RequestStatus.ACCEPTED);
        expectedUR.getRequest().setActive(false);
        expectedOffer.setCreatedAt(LocalDateTime.now());
        expectedOffer.setUserRequest(expectedUR);
        return expectedOffer;
    }

    private ListenerServiceImpl mockLS() {
        ListenerServiceImpl ls = mock(ListenerServiceImpl.class, CALLS_REAL_METHODS);
        ls.requestService = mock(RequestServiceImpl.class);
        ls.acceptedRepo = mock(AcceptedOfferRepository.class);
        ls.requestRepository = mock(RequestRepository.class);
        ls.mapperModel = mock(MapperModel.class, CALLS_REAL_METHODS);
        ls.timeUtil = mock(TimeUtil.class);
        ls.languageRepository = mock(LanguageRepository.class);
        ls.userService = mock(UserServiceImpl.class);
        return ls;
    }


}