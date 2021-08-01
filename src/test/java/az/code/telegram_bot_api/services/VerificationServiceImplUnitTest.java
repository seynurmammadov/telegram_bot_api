package az.code.telegram_bot_api.services;

import az.code.telegram_bot_api.SPRING_TEST_DATA;
import az.code.telegram_bot_api.models.DTOs.ResetPasswordDTO;
import az.code.telegram_bot_api.models.User;
import az.code.telegram_bot_api.models.VerificationToken;
import az.code.telegram_bot_api.repositories.VerificationRepo;
import az.code.telegram_bot_api.services.interfaces.UserService;
import az.code.telegram_bot_api.utils.KeycloakUtil;
import az.code.telegram_bot_api.utils.MessageUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;


import javax.mail.MessagingException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VerificationServiceImplUnitTest {

    SPRING_TEST_DATA data = new SPRING_TEST_DATA();
    VerificationServiceImpl vs;
    User user;
    VerificationToken token;

    @BeforeEach
    void setData() throws CloneNotSupportedException {
        vs = getVerificationService();
        vs.verifyPath="";
        vs.forgotPath="";
        user = data.generateUser();
        token = getVerificationToken(data.generateEmailToken(), data.generateUser());
    }

    @Test
    @DisplayName("Verification Service - reset With Token - Valid")
    public void resetWithToken() {
        when(vs.verificationRepo.findByToken(any(), any())).thenReturn(java.util.Optional.of(token));
        ResetPasswordDTO dto = data.generateResetPasswordDTO();
        assertEquals(HttpStatus.OK, vs.resetWithToken(token.getToken(), dto));
        verify(vs.keycloakUtil).setPassword(token.getUser().getUsername(), dto);
        verify(vs.verificationRepo).delete(token);
    }

    @Test
    @DisplayName("Verification Service - send Verify Token - Valid")
    public void sendVerifyToken() throws MessagingException, IOException {
        when(vs.userService.findUserByEmail(user.getEmail())).thenReturn(user);
        when(vs.verificationRepo.save(any())).thenReturn(token);
        when(vs.verificationRepo.findByToken(any(), any())).thenReturn(java.util.Optional.of(token));
        vs.sendVerifyToken(user, "");
        verify(vs.messageUtil).regVerifyNotification(user.getEmail(), token.getToken());
    }

    @Test
    @DisplayName("Verification Service - password Forgot - Valid")
    public void passwordForgot() throws MessagingException, IOException {
        when(vs.userService.findUserByEmail(user.getEmail())).thenReturn(user);
        when(vs.verificationRepo.save(any())).thenReturn(token);
        when(vs.verificationRepo.findByToken(any(), any())).thenReturn(java.util.Optional.of(token));
        assertEquals(HttpStatus.OK, vs.passwordForgot(data.generateLoginDTO(user.getEmail()), ""));
        verify(vs.messageUtil).forgot(user.getEmail(), token.getToken());
    }

    private VerificationToken getVerificationToken(VerificationToken token2, User user2) throws CloneNotSupportedException {
        VerificationToken token = (VerificationToken) token2.clone();
        token.setUser(user2);
        return token;
    }

    private VerificationServiceImpl getVerificationService() {
        VerificationServiceImpl vs = mock(VerificationServiceImpl.class, CALLS_REAL_METHODS);
        vs.userService = mock(UserService.class);
        vs.messageUtil = mock(MessageUtil.class);
        vs.verificationRepo = mock(VerificationRepo.class);
        vs.keycloakUtil = mock(KeycloakUtil.class);
        return vs;
    }

}