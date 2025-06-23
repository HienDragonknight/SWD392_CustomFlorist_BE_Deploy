package edu.fpt.customflorist.services.Email;

import edu.fpt.customflorist.models.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {
    private final JavaMailSender mailSender;
//    private String verificationLinkPage = "http://localhost:8080/custom-florist/api/v1/users/verify";
    private String verificationLinkPage = "https://custom-florist.onrender.com/custom-florist/api/v1/users/verify";
    private String emailSendFrom = "nam022831@gmail.com";

    @Override
    public void sendVerificationEmail(User user) throws Exception {
        String verificationLink = verificationLinkPage + "?code=" + user.getVerificationCode();

        String subject = "Verify Your Account";

        String content = "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }"
                + ".container { background-color: white; padding: 20px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); }"
                + "h1 { color: #007bff; }"
                + "p { color: #333; line-height: 1.6; }"
                + ".link { background-color: #007bff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; display: inline-block; margin-top: 20px; }"
                + ".link:hover { background-color: #0056b3; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<h1>Verify Your Account</h1>"
                + "<p>Hi " + user.getName() + ",</p>"
                + "<p>Please click on the following link to verify your account:</p>"
                + "<a href='" + verificationLink + "' class='link'>Verify Account</a>"
                + "<p>If you did not create an account, please ignore this email.</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        sendEmail(user.getEmail(), subject, content);
    }

    @Override
    public void sendEmail(String to, String subject, String content) throws Exception {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(emailSendFrom);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email");
        }
    }
}

