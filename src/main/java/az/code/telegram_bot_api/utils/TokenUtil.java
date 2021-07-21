package az.code.telegram_bot_api.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class TokenUtil {
    public JsonNode getUser(String auth) throws com.fasterxml.jackson.core.JsonProcessingException {
        String[] chunks = auth.split("\\.");
        String data = new String(Base64.getDecoder().decode(chunks[1]));
        return new ObjectMapper().readValue(data, JsonNode.class);
    }
}
