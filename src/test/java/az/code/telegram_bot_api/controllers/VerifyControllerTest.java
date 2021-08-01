package az.code.telegram_bot_api.controllers;

import az.code.telegram_bot_api.SPRING_TEST_DATA;
import az.code.telegram_bot_api.exceptions.InvalidPasswordException;
import az.code.telegram_bot_api.exceptions.TokenInvalidException;
import az.code.telegram_bot_api.exceptions.UserNotFoundException;
import az.code.telegram_bot_api.exceptions.UserRequestNotFound;
import az.code.telegram_bot_api.models.DTOs.LoginDTO;
import az.code.telegram_bot_api.models.DTOs.ResetPasswordDTO;
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

import static az.code.telegram_bot_api.SPRING_TEST_DATA.VERIFY_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VerifyController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class VerifyControllerTest {
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
    @DisplayName("VerifyController - verify account - HttpStatus.200")
    void verify() throws Exception {
        when(verificationService.verify(any())).thenReturn(HttpStatus.OK);
        mockMvc.perform(get(VERIFY_URL + "/confirm")
                .requestAttr("user", data.generateUserTokenDTO())
                .param("token", SPRING_TEST_DATA.userToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("VerifyController - verify account - HttpStatus.404 UserRequestNotFound()")
    void verify_test2() throws Exception {
        when(verificationService.verify(any())).thenThrow(new UserRequestNotFound());
        mockMvc.perform(get(VERIFY_URL + "/confirm")
                .requestAttr("user", data.generateUserTokenDTO())
                .param("token", SPRING_TEST_DATA.userToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserRequestNotFound))
                .andExpect(result -> assertEquals("User request not found!",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("VerifyController - verify account - HttpStatus.404 TokenInvalidException()")
    void verify_test3() throws Exception {
        when(verificationService.verify(any())).thenThrow(new TokenInvalidException());
        mockMvc.perform(get(VERIFY_URL + "/confirm")
                .requestAttr("user", data.generateUserTokenDTO())
                .param("token", SPRING_TEST_DATA.userToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TokenInvalidException))
                .andExpect(result -> assertEquals("Token invalid!",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("VerifyController - verify account - HttpStatus.404 UserNotFoundException()")
    void verify_test4() throws Exception {
        when(verificationService.verify(any())).thenThrow(new UserNotFoundException());
        mockMvc.perform(get(VERIFY_URL + "/confirm")
                .requestAttr("user", data.generateUserTokenDTO())
                .param("token", SPRING_TEST_DATA.userToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andExpect(result -> assertEquals("User not found!",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("VerifyController - forgot account password - HttpStatus.200")
    void forgot() throws Exception {
        LoginDTO loginDTO = data.generateLoginDTO("email");
        String content = mapper.writer().writeValueAsString(loginDTO);
        when(verificationService.passwordForgot(any(), any())).thenReturn(HttpStatus.OK);
        mockMvc.perform(post(VERIFY_URL + "/forgot-password")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("VerifyController - forgot account password - HttpStatus.404 UserNotFoundException()")
    void forgot_test2() throws Exception {
        LoginDTO loginDTO = data.generateLoginDTO("email");
        String content = mapper.writer().writeValueAsString(loginDTO);
        when(verificationService.passwordForgot(any(), any())).thenThrow(new UserNotFoundException());
        mockMvc.perform(post(VERIFY_URL + "/forgot-password")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andExpect(result -> assertEquals("User not found!",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("VerifyController - reset account password with token- HttpStatus.200")
    void resetWithToken() throws Exception {
        ResetPasswordDTO resetPasswordDTO = data.generateResetPasswordDTO();
        String content = mapper.writer().writeValueAsString(resetPasswordDTO);
        when(verificationService.resetWithToken(any(), any())).thenReturn(HttpStatus.OK);
        mockMvc.perform(post(VERIFY_URL + "/reset-password/{token}", SPRING_TEST_DATA.userToken)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("VerifyController - reset account password with token- HttpStatus.404 TokenInvalidException()")
    void resetWithToken_test2() throws Exception {
        ResetPasswordDTO resetPasswordDTO = data.generateResetPasswordDTO();
        String content = mapper.writer().writeValueAsString(resetPasswordDTO);
        when(verificationService.resetWithToken(any(), any())).thenThrow(new TokenInvalidException());
        mockMvc.perform(post(VERIFY_URL + "/reset-password/{token}", SPRING_TEST_DATA.userToken)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TokenInvalidException))
                .andExpect(result -> assertEquals("Token invalid!",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()))

                .andDo(print());
    }

    @Test
    @DisplayName("VerifyController - reset account password with token- HttpStatus.404 UserNotFoundException()")
    void resetWithToken_test3() throws Exception {
        ResetPasswordDTO resetPasswordDTO = data.generateResetPasswordDTO();
        String content = mapper.writer().writeValueAsString(resetPasswordDTO);
        when(verificationService.resetWithToken(any(), any())).thenThrow(new UserNotFoundException());
        mockMvc.perform(post(VERIFY_URL + "/reset-password/{token}", SPRING_TEST_DATA.userToken)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andExpect(result -> assertEquals("User not found!",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }
    @Test
    @DisplayName("VerifyController - reset account with old password - HttpStatus.200")
    void resetWithOldPassword() throws Exception {
        ResetPasswordDTO resetPasswordDTO = data.generateResetPasswordDTO();
        String content = mapper.writer().writeValueAsString(resetPasswordDTO);
        when(verificationService.resetWithOldPassword(any(),any())).thenReturn(HttpStatus.OK);
        mockMvc.perform(post(VERIFY_URL + "/reset-password")
                .requestAttr("user", data.generateUserTokenDTO())
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("VerifyController - reset account with old password - HttpStatus.401 InvalidPasswordException()")
    void resetWithOldPassword_test2() throws Exception {
        ResetPasswordDTO resetPasswordDTO = data.generateResetPasswordDTO();
        String content = mapper.writer().writeValueAsString(resetPasswordDTO);
        when(verificationService.resetWithOldPassword(any(),any())).thenThrow(new InvalidPasswordException());
        mockMvc.perform(post(VERIFY_URL + "/reset-password")
                .requestAttr("user", data.generateUserTokenDTO())
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidPasswordException))
                .andExpect(result -> assertEquals("Invalid password!",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }
    @Test
    @DisplayName("VerifyController - reset account with old password - HttpStatus.404 UserNotFoundException()")
    void resetWithOldPassword_test3() throws Exception {
        ResetPasswordDTO resetPasswordDTO = data.generateResetPasswordDTO();
        String content = mapper.writer().writeValueAsString(resetPasswordDTO);
        when(verificationService.resetWithOldPassword(any(),any())).thenThrow(new UserNotFoundException());
        mockMvc.perform(post(VERIFY_URL + "/reset-password")
                .requestAttr("user", data.generateUserTokenDTO())
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andExpect(result -> assertEquals("User not found!",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }

}