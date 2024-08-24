package kma.cnwat.be.domain.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final JavaMailSender javaMailSender;

    public NotificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendActivationEmail(String to, String subject, String activateLink) {
        try {
            // Tạo một đối tượng MimeMessage
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            // Tạo một MimeMessageHelper
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // Thiết lập thông tin email
            helper.setTo(to);
            helper.setSubject(subject);

            // Nội dung email HTML
            String emailContent = "<html>" +
                    "<body style='font-family: Arial, sans-serif;'>" +
                    "<h2 style='color: #4CAF50;'>Welcome to Our Service!</h2>" +
                    "<p>Hi there,</p>" +
                    "<p>Thank you for signing up. To activate your account, please click the link below:</p>" +
                    "<p><a href=\"" + activateLink + "\" style='color: #ffffff; background-color: #4CAF50; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Activate Your Account</a></p>" +
                    "<p>If you did not create an account, please ignore this email.</p>" +
                    "<p>Best regards,<br/>The Team</p>" +
                    "</body>" +
                    "</html>";

            // Thiết lập nội dung email là HTML
            helper.setText(emailContent, true);

            // Gửi email
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
            // Xử lý lỗi khi gửi email
        }
    }


    public void sendResetPasswordEmail(String to, String subject, String resetLink) {
        try {
            // Tạo một đối tượng MimeMessage
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            // Tạo một MimeMessageHelper
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // Thiết lập thông tin email
            helper.setTo(to);
            helper.setSubject(subject);

            // Nội dung email HTML
            String emailContent = "<html>" +
                    "<body>" +
                    "<h3>Reset Your Password</h3>" +
                    "<p>To reset your password, click the link below:</p>" +
                    "<a href=\"" + resetLink + "\">Reset Password</a>" +
                    "<p>If you did not request a password reset, please ignore this email.</p>" +
                    "</body>" +
                    "</html>";

            // Thiết lập nội dung email
            helper.setText(emailContent, true);

            // Gửi email
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
            // Xử lý lỗi khi gửi email
            throw new RuntimeException("Failed to send reset password email", e);
        }
    }


}
