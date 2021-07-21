package az.code.telegram_bot_api.utils;

import az.code.telegram_bot_api.models.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MessageUtil {


    final
    MailSenderUtil mailSenderUtil;


    public MessageUtil(MailSenderUtil mailSenderUtil) {
        this.mailSenderUtil = mailSenderUtil;
    }

    //TODO add html page
    public void regVerifyNotification(User user, String token) {
        mailSenderUtil.sendEmail(user.getEmail(), "registrationVerifySubject",
                token);
    }
}
