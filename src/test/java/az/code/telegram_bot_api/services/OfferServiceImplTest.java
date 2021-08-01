package az.code.telegram_bot_api.services;

import az.code.telegram_bot_api.SPRING_TEST_DATA;
import az.code.telegram_bot_api.configs.RabbitMQConfig;
import az.code.telegram_bot_api.models.AgencyOffer;
import az.code.telegram_bot_api.models.Offer;
import az.code.telegram_bot_api.models.User;
import az.code.telegram_bot_api.models.UserRequest;
import az.code.telegram_bot_api.models.enums.RequestStatus;
import az.code.telegram_bot_api.models.mapper.MapperModel;
import az.code.telegram_bot_api.repositories.OfferRepository;
import az.code.telegram_bot_api.utils.ConverterUtil;
import az.code.telegram_bot_api.utils.TimeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OfferServiceImplTest {

    SPRING_TEST_DATA data = new SPRING_TEST_DATA();
    OfferServiceImpl os;
    UserRequest ur;

    @BeforeEach
    void setData() {
        os = mock(OfferServiceImpl.class, CALLS_REAL_METHODS);
        ur = getUserRequest();
    }

    @Test
    @DisplayName("Offer Service - send Offer Valid")
    void sendOffer() throws IOException, CloneNotSupportedException {
        Offer offer = data.generateOffer();
        mockOfferService(os);

        when(os.requestService.getForOffer(anyString(), anyLong())).thenReturn(ur);
        when(os.mapperModel.defaultMap(any(), any())).thenReturn(offer);

        Offer expectedOffer = (Offer) offer.clone();
        UserRequest expectedUR = (UserRequest) ur.clone();
        expectedOffer.setUserRequest(expectedUR);
        expectedUR.setOffer(offer);
        expectedUR.setRequestStatus(RequestStatus.OFFER_MADE);

        assertEquals(HttpStatus.OK, os.sendOffer(data.generateUserDTO(data.generateUser()), 1L, null));

        verify(os.offerRepository).save(expectedOffer);
        verify(os.requestService).save(expectedUR);
        verify(os).sendOffer(expectedUR);
    }


    @Test
    @DisplayName("Offer Service - send Offer rabbit mq Valid")
    void sendOfferRabbitMQ() throws IOException {
        mockOfferServiceForRabbit(os);

        when(os.converterUtil.htmlToImage(anyString(), any(), anyString())).thenReturn(new byte[2]);

        assertEquals(HttpStatus.OK, os.sendOffer(ur));

        verify(os.converterUtil).htmlToImage("path", ur.getOffer(), ur.getUser().getCompany_name());
        verify(os.template).convertAndSend(RabbitMQConfig.exchange,
                RabbitMQConfig.offered,
                AgencyOffer.builder()
                        .UUID(ur.getRequest().getUUID())
                        .file(new byte[2])
                        .username(ur.getUser().getUsername())
                        .build());
    }

    private UserRequest getUserRequest() {
        User user = data.generateUser();
        return data.generateUserRequest(data.generateRequest(), user, RequestStatus.NEW_REQUEST, false);
    }

    private void mockOfferService(OfferServiceImpl os) {
        os.timeUtil = mock(TimeUtil.class);
        os.requestService = mock(RequestServiceImpl.class);
        os.mapperModel = mock(MapperModel.class);
        os.offerRepository = mock(OfferRepository.class);
        os.requestService = mock(RequestServiceImpl.class);
        mockOfferServiceForRabbit(os);
    }

    private void mockOfferServiceForRabbit(OfferServiceImpl os) {
        os.converterUtil = mock(ConverterUtil.class);
        os.templatePath = "path";
        os.template = mock(RabbitTemplate.class);
    }

}