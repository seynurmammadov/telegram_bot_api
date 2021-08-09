package az.code.telegram_bot_api.controllers;

import az.code.telegram_bot_api.SPRING_TEST_DATA;
import az.code.telegram_bot_api.exceptions.InvalidUserDataException;
import az.code.telegram_bot_api.exceptions.VerifyEmailException;
import az.code.telegram_bot_api.models.DTOs.LoginDTO;
import az.code.telegram_bot_api.models.DTOs.RegistrationDTO;
import az.code.telegram_bot_api.models.DTOs.TokenDTO;
import az.code.telegram_bot_api.services.interfaces.AuthService;
import az.code.telegram_bot_api.services.interfaces.VerificationService;
import az.code.telegram_bot_api.utils.MessageUtil;
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

import java.util.Objects;

import static az.code.telegram_bot_api.SPRING_TEST_DATA.AUTH_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class AuthControllerTest {
    SPRING_TEST_DATA data = new SPRING_TEST_DATA();

    public static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @MockBean
    VerificationService verificationService;

    @MockBean
    MessageUtil messageUtil;


    @BeforeAll
    static void start() {
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    }


    @Test
    @DisplayName("AuthController - login - HttpStatus.200")
    void login_test1() throws Exception {
        LoginDTO loginDTO = data.generateLoginDTO("email@gmail.com");
        TokenDTO response = TokenDTO.builder().access_token(SPRING_TEST_DATA.userToken).build();
        String content = mapper.writer().writeValueAsString(loginDTO);

        when(authService.login(any())).thenReturn(response);
        mockMvc.perform(post(AUTH_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content)
                .characterEncoding("utf-8"))
                .andExpect(content().string(mapper.writeValueAsString(response)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("AuthController - login - HttpStatus.401 InvalidUserDataException() ")
    void login_test2() throws Exception {
        LoginDTO loginDTO = data.generateLoginDTO("email@gmail.com");
        String content = mapper.writer().writeValueAsString(loginDTO);

        when(authService.login(any())).thenThrow(new InvalidUserDataException());
        mockMvc.perform(post(AUTH_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content))
                .andExpect(status().isUnauthorized())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidUserDataException))
                .andExpect(result -> assertEquals("Invalid email or password!",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());

    }

    @Test
    @DisplayName("AuthController - login - HttpStatus.401 VerifyEmailException() ")
    void login_test3() throws Exception {
        LoginDTO loginDTO = data.generateLoginDTO("email@gmail.com");
        String content = mapper.writer().writeValueAsString(loginDTO);

        when(authService.login(any())).thenThrow(new VerifyEmailException());
        mockMvc.perform(post(AUTH_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content))
                .andExpect(status().isForbidden())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof VerifyEmailException))
                .andExpect(result -> assertEquals("Verify email before login!",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("AuthController - register - HttpStatus.200 ")
    void register() throws Exception {
        RegistrationDTO registrationDTO = data.generateRegistrationDTO();

        String content = mapper.writer().writeValueAsString(registrationDTO);
        when(authService.registration(any(), any())).thenReturn(HttpStatus.OK);
        mockMvc.perform(post(AUTH_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content))
                .andExpect(status().isOk())
                .andDo(print());
    }
}