package az.code.telegram_bot_api.utils;

import org.springframework.stereotype.Component;

@Component
public class MessageUtil {


    final
    MailSenderUtil mailSenderUtil;


    public MessageUtil(MailSenderUtil mailSenderUtil) {
        this.mailSenderUtil = mailSenderUtil;
    }

    //TODO add html page
    public void regVerifyNotification(String email, String token) {
        mailSenderUtil.sendEmail(email, "registrationVerifySubject",
                token);
    }

    public void forgot(String email, String token) {
        mailSenderUtil.sendEmail(email, "registrationVerifySubject",
                token);
    }
}
