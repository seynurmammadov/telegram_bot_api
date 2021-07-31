package az.code.telegram_bot_api.models.mapper;

import az.code.telegram_bot_api.SPRING_TEST_DATA;
import az.code.telegram_bot_api.models.DTOs.OfferDTO;
import az.code.telegram_bot_api.models.DTOs.RegistrationDTO;
import az.code.telegram_bot_api.models.DTOs.RequestDTO;
import az.code.telegram_bot_api.models.DTOs.UserData;
import az.code.telegram_bot_api.models.Language;
import az.code.telegram_bot_api.models.Offer;
import az.code.telegram_bot_api.models.User;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MapperModelImplTest {

    SPRING_TEST_DATA data = new SPRING_TEST_DATA();
    MapperModelImpl mm;

    @BeforeEach
    void setData() {
        mm = new MapperModelImpl(new ModelMapper());
    }

    @Test
    void entityToDTO() {
        MapperModelImpl mm = new MapperModelImpl(new ModelMapper());
        RegistrationDTO dto = data.generateRegistrationDTO();
        assertEquals(data.toUser(dto), mm.entityToDTO(dto, User.class));
    }

    @Test
    @DisplayName("Mapper model- default map - Valid")
    void defaultMap_test1() {
        OfferDTO offerDTO = data.generateOfferDTO();
        assertEquals(data.generateOffer(), mm.defaultMap(offerDTO, Offer.class));
    }

    @Test
    @DisplayName("Mapper model- default map - Valid")
    void defaultMap_test2() {
        UserData userData = data.generateUserData();
        assertEquals(data.toRequestDTO(userData.getAnswers()), mm.defaultMap(userData.getAnswers(), RequestDTO.class));
    }

    @Test
    @DisplayName("Mapper model- requestDTO to Request - Valid")
    void requestDTOtoRequest() {
        LocalDateTime time = LocalDateTime.now();
        Language language = data.generateLanguage();
        RequestDTO requestDTO = data.generateRequestDTO();
        assertEquals(data.toRequest(requestDTO), mm.requestDTOtoRequest(requestDTO, language, time));
    }

}