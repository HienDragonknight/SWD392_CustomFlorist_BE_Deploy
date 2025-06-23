package edu.fpt.customflorist.services.Email;

import edu.fpt.customflorist.models.User;

public interface IEmailService {
    void sendVerificationEmail(User user) throws Exception;
    void sendEmail(String to, String subject, String content) throws Exception;
}
