package kma.cnpm.beapp.domain.user.dto.resquest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kma.cnpm.beapp.domain.common.validation.EmailValidation;
import lombok.Getter;

@Getter
public class CreateUserRequest {

    @NotBlank(message = "Email không được để trống")
    @EmailValidation(message = "Email phải hợp lệ")
    private String email;  // Email người dùng

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, message = "Mật khẩu phải chứa ít nhất 8 ký tự")
    private String password;  // Mật khẩu

    @NotBlank(message = "Mật khẩu xác nhận không được để trống")
    @Size(min = 8, message = "Mật khẩu xác nhận phải chứa ít nhất 8 ký tự")
    private String confirmPassword;  // Xác nhận mật khẩu

    @NotBlank(message = "Tên đầy đủ không được để trống")
    @Size(min = 2, max = 50, message = "Tên đầy đủ phải có từ 2 đến 50 ký tự")
    private String fullName;  // Tên đầy đủ của người dùng

    @NotBlank(message = "Captcha token không được để trống")
    private String captchaToken;  // Captcha token để xác thực
}
