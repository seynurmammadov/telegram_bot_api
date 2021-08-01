package az.code.telegram_bot_api.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.mockito.Mockito.*;

class MailSenderUtilTest {

    JavaMailSender javaMailSender;
    MailSenderUtil mailSender;

    @BeforeEach
    public void setup() {
        javaMailSender = mock(JavaMailSender.class);
        mailSender = new MailSenderUtil(javaMailSender);

    }

    @Test
    @DisplayName("MailSenderUtil - send email Valid")
    void sendEmail() throws MessagingException {
        String to = "to";
        String subject = "subject";
        String content = "content";
        JavaMailSender ms = new JavaMailSenderImpl();
        MimeMessage mimeMessage = ms.createMimeMessage();
        when(mailSender.javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        mailSender.sendEmail(to, subject, content);

        verify(javaMailSender).send(getSimpleMailMessage(mimeMessage,to, subject, content));
    }

    private MimeMessage getSimpleMailMessage( MimeMessage mimeMessage,String to, String subject, String content) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(content, true);
        helper.setTo(to);
        helper.setSubject(subject);
        return mimeMessage;
    }

}