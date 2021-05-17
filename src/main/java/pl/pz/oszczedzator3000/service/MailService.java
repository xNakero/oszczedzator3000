package pl.pz.oszczedzator3000.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.pz.oszczedzator3000.exceptions.registration.RegistrationFailedException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String recipient, String subject, String text) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setText(text, false);
            helper.setSubject(subject);
            helper.setTo(recipient);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException | MailAuthenticationException e) {
            logger.debug(e.getMessage());
            throw new RegistrationFailedException();
        }
    }


}
