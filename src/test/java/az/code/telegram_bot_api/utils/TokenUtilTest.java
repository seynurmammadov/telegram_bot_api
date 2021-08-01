package az.code.telegram_bot_api.utils;

import az.code.telegram_bot_api.SPRING_TEST_DATA;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class TokenUtilTest {

    @Test
    @DisplayName("TokenUtil - getUser Valid")
    void getUser() throws JsonProcessingException {
        String token = SPRING_TEST_DATA.userToken;
        String expected = "{\"exp\":1627460153,\"iat\":1627424153,\"jti\":\"bae84d44-74f6-4075-b681-212df3de8506\",\"iss\":\"http://localhost:8080/auth/realms/tour_bot\",\"aud\":\"account\",\"sub\":\"cf8a4ab3-8417-459b-a921-3329f6e6606b\",\"typ\":\"Bearer\",\"azp\":\"tour_bot_microservice\",\"session_state\":\"a759bb7f-e645-4d6d-85bc-66e3724531ce\",\"acr\":\"1\",\"allowed-origins\":[\"http://localhost:5000\"],\"realm_access\":{\"roles\":[\"default-roles-tour_bot\",\"offline_access\",\"uma_authorization\",\"app-user\"]},\"resource_access\":{\"tour_bot_microservice\":{\"roles\":[\"user\"]},\"account\":{\"roles\":[\"manage-account\",\"manage-account-links\",\"view-profile\"]}},\"scope\":\"profile email\",\"email_verified\":true,\"name\":\"Seynur Mammadov\",\"preferred_username\":\"seynur_mmc_25fefa70-a469-4c0c-88f5-2ded1622ff9e\",\"given_name\":\"Seynur\",\"family_name\":\"Mammadov\",\"email\":\"seynursm@code.edu.az\"}";
        TokenUtil tokenUtil = new TokenUtil();
        assertEquals(new ObjectMapper().readValue(expected, JsonNode.class), tokenUtil.getUser(token));
    }

}