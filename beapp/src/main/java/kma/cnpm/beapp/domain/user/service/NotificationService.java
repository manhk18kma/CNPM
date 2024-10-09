//package kma.cnpm.beapp.domain.user.service;
//
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//@Service
//public class NotificationService {
//
//    private final JavaMailSender javaMailSender;
//
//    public NotificationService(JavaMailSender javaMailSender) {
//        this.javaMailSender = javaMailSender;
//    }
//    @Async
//    public void sendActivationEmail(String to, String subject, String activateLink) {
//        try {
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//
//            helper.setTo(to);
//            helper.setSubject(subject);
//
//            String emailContent = "<html>" +
//                    "<body style='font-family: Arial, sans-serif;'>" +
//                    "<h2 style='color: #4CAF50;'>Welcome to Our Service!</h2>" +
//                    "<p>Hi there,</p>" +
//                    "<p>Thank you for signing up. To activate your account, please click the link below:</p>" +
//                    "<p><a href=\"" + activateLink + "\" style='color: #ffffff; background-color: #4CAF50; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Activate Your Account</a></p>" +
//                    "<p>If you did not create an account, please ignore this email.</p>" +
//                    "<p>Best regards,<br/>The Team</p>" +
//                    "</body>" +
//                    "</html>";
//
//            // Thiết lập nội dung email là HTML
//            helper.setText(emailContent, true);
//
//            // Gửi email
//            javaMailSender.send(mimeMessage);
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//            // Xử lý lỗi khi gửi email
//        }
//    }
//
//    @Async
//    public void sendResetPasswordEmail(String to, String subject, String resetLink) {
//        try {
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//
//            helper.setTo(to);
//            helper.setSubject(subject);
//
//            // Nội dung email HTML
//            String emailContent = "<html>" +
//                    "<body>" +
//                    "<h3>Reset Your Password</h3>" +
//                    "<p>To reset your password, click the link below:</p>" +
//                    "<a href=\"" + resetLink + "\">Reset Password</a>" +
//                    "<p>If you did not request a password reset, please ignore this email.</p>" +
//                    "</body>" +
//                    "</html>";
//
//            // Thiết lập nội dung email
//            helper.setText(emailContent, true);
//
//            // Gửi email
//            javaMailSender.send(mimeMessage);
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//            // Xử lý lỗi khi gửi email
//            throw new RuntimeException("Failed to send reset password email", e);
//        }
//    }
//
//
//}



package kma.cnpm.beapp.domain.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final JavaMailSender javaMailSender;

    public NotificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendActivationEmail(String to, String subject, String activateLink) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);

            // Nội dung email HTML
            String emailContent = "<html>" +
                    "<head>" +
                    "<style>" +
                    "body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }" +
                    ".container { max-width: 600px; margin: auto; background: #ffffff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }" +
                    "h2 { color: #4CAF50; }" +
                    "p { font-size: 16px; }" +
                    ".button { display: inline-block; padding: 10px 20px; color: #ffffff; background-color: #4CAF50; text-decoration: none; border-radius: 5px; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<h2>Chào mừng đến với Dịch vụ của Chúng tôi!</h2>" +
                    "<p>Xin chào,</p>" +
                    "<p>Chúng tôi cảm ơn bạn đã đăng ký. Để kích hoạt tài khoản của bạn, vui lòng nhấp vào liên kết dưới đây:</p>" +
                    "<p><a href=\"" + activateLink + "\" class='button'>Kích hoạt tài khoản của bạn</a></p>" +
                    "<p>Nếu bạn không tạo tài khoản, xin vui lòng bỏ qua email này.</p>" +
                    "<p>Trân trọng,<br/>Đội ngũ hỗ trợ</p>" +
                    "</div>" +
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

    @Async
    public void sendResetPasswordEmail(String to, String subject, String resetLink) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);

            // Nội dung email HTML
            String emailContent = "<html>" +
                    "<head>" +
                    "<style>" +
                    "body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }" +
                    ".container { max-width: 600px; margin: auto; background: #ffffff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }" +
                    "h3 { color: #FF5722; }" +
                    "p { font-size: 16px; }" +
                    ".button { display: inline-block; padding: 10px 20px; color: #ffffff; background-color: #FF5722; text-decoration: none; border-radius: 5px; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<h3>Đặt lại mật khẩu của bạn</h3>" +
                    "<p>Để đặt lại mật khẩu, vui lòng nhấp vào liên kết dưới đây:</p>" +
                    "<p><a href=\"" + resetLink + "\" class='button'>Đặt lại mật khẩu</a></p>" +
                    "<p>Nếu bạn không yêu cầu đặt lại mật khẩu, xin vui lòng bỏ qua email này.</p>" +
                    "<p>Trân trọng,<br/>Đội ngũ hỗ trợ</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            // Thiết lập nội dung email
            helper.setText(emailContent, true);

            // Gửi email
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
            // Xử lý lỗi khi gửi email
            throw new RuntimeException("Không thể gửi email đặt lại mật khẩu", e);
        }
    }
}
