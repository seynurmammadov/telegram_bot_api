package az.code.telegram_bot_api.services;

import az.code.telegram_bot_api.SPRING_TEST_DATA;
import az.code.telegram_bot_api.models.User;
import az.code.telegram_bot_api.models.VerificationToken;
import az.code.telegram_bot_api.models.enums.TokenType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VerificationServiceImplTest {
    @Autowired
    VerificationServiceImpl verificationService;
    SPRING_TEST_DATA data = new SPRING_TEST_DATA();

    @Test
    @DisplayName("Verification Service - clear tokens - Valid")
    void clearTokens_test1() {
        List<VerificationToken> tokens = getVerificationTokens(data.generateEmailToken(), TokenType.EMAIL_VERIFY);
        assertEquals(0, tokens.size());
    }

    @Test
    @DisplayName("Verification Service - clear tokens - Valid")
    void clearTokens_test2() {
        List<VerificationToken> tokens = getVerificationTokens(data.generatePasswordToken(), TokenType.PASSWORD_RESET);
        assertEquals(1, tokens.size());
    }

    @Test
    @DisplayName("Verification Service - findByToken tokens - Valid")
    void findByToken_test1() {
        User user = getUserTokens(data.generateEmailToken());
        VerificationToken expectedToken = user.getVerificationToken().get(0);
        assertEquals(expectedToken, verificationService.findByToken(expectedToken.getToken(), expectedToken.getTokenType()));
        verificationService.clearTokens(user, TokenType.EMAIL_VERIFY);
    }

    @Test
    @DisplayName("Verification Service - findByToken tokens - Valid")
    void findByToken_test2() {
        User user = getUserTokens(data.generateEmailToken());
        VerificationToken expectedToken = user.getVerificationToken().get(0);
        assertFalse(findByToken(expectedToken.getTokenType()));
        verificationService.clearTokens(user, TokenType.EMAIL_VERIFY);
    }

    private boolean findByToken(TokenType tokenType) {
        try {
            verificationService.findByToken("PASSWORD_RESET", tokenType);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    void setTokens(User user, VerificationToken token, VerificationToken token2) {
        verificationService.userService.save(user);
        List<VerificationToken> tokens = getVerificationTokens(user, token, token2);
        user.setVerificationToken(tokens);
    }

    private List<VerificationToken> getVerificationTokens(User user, VerificationToken token, VerificationToken token2) {
        List<VerificationToken> verificationTokens = new ArrayList<>();
        token.setUser(user);
        token2.setUser(user);
        verificationService.verificationRepo.save(token);
        verificationService.verificationRepo.save(token2);
        verificationTokens.add(token);
        verificationTokens.add(token2);
        return verificationTokens;
    }

    private List<VerificationToken> getVerificationTokens(VerificationToken randomEmailToken, TokenType emailVerify) {
        User user = getUserTokens(randomEmailToken);
        verificationService.clearTokens(user, emailVerify);
        return (List<VerificationToken>) verificationService.verificationRepo.findAll();
    }

    private User getUserTokens(VerificationToken randomEmailToken) {
        User user = data.generateUser();
        setTokens(user, data.generateEmailToken(), randomEmailToken);
        return user;
    }
}