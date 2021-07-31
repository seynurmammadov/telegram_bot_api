package az.code.telegram_bot_api.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class MailSenderUtilTest {

    JavaMailSender javaMailSender;
    MailSenderUtil mailSender;

    @BeforeEach
    public void setup() {
        javaMailSender = mock(JavaMailSender.class);
        mailSender = new MailSenderUtil(javaMailSender);
    }

    @Test
    @DisplayName("MailSenderUtil - send email - Valid")
    void sendEmail() {
        String to = "to";
        String subject = "subject";
        String content = "content";
        mailSender.sendEmail(to, subject, content);
        verify(javaMailSender).send(getSimpleMailMessage(to, subject, content));
    }

    private SimpleMailMessage getSimpleMailMessage(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        return message;
    }

}