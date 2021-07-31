package az.code.telegram_bot_api.utils;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class MessageUtil {

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

    public String getUrl(HttpServletRequest request) {
        return request.getRequestURL()
                .substring(0, request.getRequestURL().length() - request.getRequestURI().length())
                + request.getContextPath();
    }

}
