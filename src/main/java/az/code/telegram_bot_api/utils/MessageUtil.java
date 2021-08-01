package az.code.telegram_bot_api.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class MessageUtil {

    @Value("${mail.html.template.verify}")
    String verifyTemplate;
    @Value("${mail.html.template.forgot}")
    String forgotTemplate;
    @Value("${mail.subject.verify}")
    String verifySubject;
    @Value("${mail.subject.forgot}")
    String forgotSubject;
    ConverterUtil converterUtil;
    MailSenderUtil mailSenderUtil;

    public MessageUtil(MailSenderUtil mailSenderUtil, ConverterUtil converterUtil) {
        this.mailSenderUtil = mailSenderUtil;
        this.converterUtil = converterUtil;
    }

    public void regVerifyNotification(String email, String token) throws MessagingException, IOException {
        String mail = converterUtil.htmlToEmail(verifyTemplate, token);
        mailSenderUtil.sendEmail(email, verifySubject, mail);
    }

    public void forgot(String email, String token) throws MessagingException, IOException {
        String mail = converterUtil.htmlToEmail(forgotTemplate, token);
        mailSenderUtil.sendEmail(email, forgotSubject,mail);
    }

    public String getUrl(HttpServletRequest request) {
        return request.getRequestURL()
                .substring(0, request.getRequestURL().length() - request.getRequestURI().length())
                + request.getContextPath();
    }

}
