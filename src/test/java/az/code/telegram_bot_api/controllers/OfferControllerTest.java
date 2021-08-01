package az.code.telegram_bot_api.controllers;

import az.code.telegram_bot_api.SPRING_TEST_DATA;
import az.code.telegram_bot_api.models.DTOs.OfferDTO;
import az.code.telegram_bot_api.services.interfaces.OfferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static az.code.telegram_bot_api.SPRING_TEST_DATA.OFFER_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OfferController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class OfferControllerTest {
    SPRING_TEST_DATA data = new SPRING_TEST_DATA();

    public static final ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    static void start() {
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    }


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    OfferService offerService;

    @Test
    @DisplayName("OfferController - send offer - HttpStatus.200")
    void sendOffer() throws Exception {

        OfferDTO offerDTO = data.generateOfferDTO();
        String content = mapper.writer().writeValueAsString(offerDTO);

        when(offerService.sendOffer(any(), any(), any())).thenReturn(HttpStatus.OK);
        mockMvc.perform(post(OFFER_URL + "/{userRequestId}", 5)
                .requestAttr("user", data.generateUserTokenDTO())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content))
                .andExpect(status().isOk())
                .andDo(print());
    }
}