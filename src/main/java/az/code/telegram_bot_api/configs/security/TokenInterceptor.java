package az.code.telegram_bot_api.configs.security;

import az.code.telegram_bot_api.models.UserTokenDTO;
import az.code.telegram_bot_api.utils.TokenUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {
    final
    TokenUtil tokenUtil;

    public TokenInterceptor(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String auth = request.getHeader("Authorization");
        if (auth != null) {
            JsonNode user = tokenUtil.getUser(auth);
            request.setAttribute("user",
                    UserTokenDTO.builder()
                            .agent_name(user.get("given_name").asText())
                            .agent_surname(user.get("family_name").asText())
                            .email(user.get("email").asText())
                            .username(user.get("preferred_username").asText())
                            .verified(user.get("email_verified").asBoolean())
                            .build());
        }
        return true;
    }

}