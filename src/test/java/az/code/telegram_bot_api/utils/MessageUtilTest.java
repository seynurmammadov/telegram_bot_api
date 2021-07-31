package az.code.telegram_bot_api.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;


import static org.junit.jupiter.api.Assertions.*;

class MessageUtilTest {

    @Test
    @DisplayName("TimeUtilTest - get url - Valid")
    void getUrl() {
        MessageUtil messageUtil = new MessageUtil(null);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerPort(5000);
        assertEquals("http://localhost:5000", messageUtil.getUrl(request));
    }

}