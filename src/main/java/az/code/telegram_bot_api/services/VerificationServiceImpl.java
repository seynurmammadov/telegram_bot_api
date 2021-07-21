package az.code.telegram_bot_api.services;

import az.code.telegram_bot_api.exceptions.IncorrectVerifyTokenException;
import az.code.telegram_bot_api.models.User;
import az.code.telegram_bot_api.models.VerificationToken;
import az.code.telegram_bot_api.repositories.VerificationRepo;
import az.code.telegram_bot_api.services.interfaces.VerificationService;
import az.code.telegram_bot_api.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
public class VerificationServiceImpl implements VerificationService {

    final
    VerificationRepo verificationRepo;
    final
    MessageUtil messageUtil;

    public VerificationServiceImpl(VerificationRepo verificationRepo, MessageUtil messageUtil) {
        this.verificationRepo = verificationRepo;
        this.messageUtil = messageUtil;
    }

    @Override
    public void sendVerifyToken(User user) {
        String token = verificationRepo
                .save(VerificationToken.builder()
                        .createdDate(LocalDateTime.now())
                        .user(user)
                        .token(UUID.randomUUID().toString())
                        .build())
                .getToken();
        messageUtil.regVerifyNotification(user, token);
    }
    @Override
    public VerificationToken findByToken(String token) {
        Optional<VerificationToken> vrfyToken = verificationRepo.findByToken(token);
        if (vrfyToken.isPresent()) {
            return vrfyToken.get();
        } else
            throw new IncorrectVerifyTokenException();
    }
    @Override
    public void delete(VerificationToken token){
        verificationRepo.delete(token);
    }
}
