package az.code.telegram_bot_api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class TokenUtilTest {

    @Test
    @DisplayName("TokenUtil - getUser - Valid")
    void getUser() throws JsonProcessingException {
        String token = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2NkpVTlVMRU5NUEE2d2ZuN205cWx3blNmcVBHd3RfMFgzV1FsZnJzWnNzIn0.eyJleHAiOjE2Mjc0NjAxNTMsImlhdCI6MTYyNzQyNDE1MywianRpIjoiYmFlODRkNDQtNzRmNi00MDc1LWI2ODEtMjEyZGYzZGU4NTA2IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgvcmVhbG1zL3RvdXJfYm90IiwiYXVkIjoiYWNjb3VudCIsInN1YiI6ImNmOGE0YWIzLTg0MTctNDU5Yi1hOTIxLTMzMjlmNmU2NjA2YiIsInR5cCI6IkJlYXJlciIsImF6cCI6InRvdXJfYm90X21pY3Jvc2VydmljZSIsInNlc3Npb25fc3RhdGUiOiJhNzU5YmI3Zi1lNjQ1LTRkNmQtODViYy02NmUzNzI0NTMxY2UiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6NTAwMCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy10b3VyX2JvdCIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iLCJhcHAtdXNlciJdfSwicmVzb3VyY2VfYWNjZXNzIjp7InRvdXJfYm90X21pY3Jvc2VydmljZSI6eyJyb2xlcyI6WyJ1c2VyIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6IlNleW51ciBNYW1tYWRvdiIsInByZWZlcnJlZF91c2VybmFtZSI6InNleW51cl9tbWNfMjVmZWZhNzAtYTQ2OS00YzBjLTg4ZjUtMmRlZDE2MjJmZjllIiwiZ2l2ZW5fbmFtZSI6IlNleW51ciIsImZhbWlseV9uYW1lIjoiTWFtbWFkb3YiLCJlbWFpbCI6InNleW51cnNtQGNvZGUuZWR1LmF6In0.RVtJzRHYUcXmqh1KO8iOjk1-AlTfwElPJDRY_GT0hqwjBAxvy2fe8N3WWa0qUtBWry4e_PJ5Eu4-8XnAZpTGPfwMIbOtq51v1jXQO0Bjw2euMN-pPK7G-dVumrhGrscABv8PILfpMVjEhDdTFVnCOtXl00P4WBMbb1gdQIr-eJkwJwPQCMdZdm2_eZb8A0iaYVoJ95XK-llCOfB9CTofr5dtW_2pCZYi91TlmjdpsdZt44jEozzthjQL03dnR9Ten3wfjP9W3ZZKBF11qzedWCgnGgmJKsd-dKGDO96w_ZTItTzfbwDrgF3ayJLXrDdZZcRvt1dFzxtwiDg5RHSG1A";
        String data = "{\"exp\":1627460153,\"iat\":1627424153,\"jti\":\"bae84d44-74f6-4075-b681-212df3de8506\",\"iss\":\"http://localhost:8080/auth/realms/tour_bot\",\"aud\":\"account\",\"sub\":\"cf8a4ab3-8417-459b-a921-3329f6e6606b\",\"typ\":\"Bearer\",\"azp\":\"tour_bot_microservice\",\"session_state\":\"a759bb7f-e645-4d6d-85bc-66e3724531ce\",\"acr\":\"1\",\"allowed-origins\":[\"http://localhost:5000\"],\"realm_access\":{\"roles\":[\"default-roles-tour_bot\",\"offline_access\",\"uma_authorization\",\"app-user\"]},\"resource_access\":{\"tour_bot_microservice\":{\"roles\":[\"user\"]},\"account\":{\"roles\":[\"manage-account\",\"manage-account-links\",\"view-profile\"]}},\"scope\":\"profile email\",\"email_verified\":true,\"name\":\"Seynur Mammadov\",\"preferred_username\":\"seynur_mmc_25fefa70-a469-4c0c-88f5-2ded1622ff9e\",\"given_name\":\"Seynur\",\"family_name\":\"Mammadov\",\"email\":\"seynursm@code.edu.az\"}";
        TokenUtil tokenUtil = new TokenUtil();
        assertEquals(new ObjectMapper().readValue(data, JsonNode.class), tokenUtil.getUser(token));
    }
}