package kma.cnpm.beapp.domain.user.dto.resquest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResetPasswordRequest {
    @NotBlank(message = "Secret key không được để trống")
    private String secretKey;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, message = "Mật khẩu phải chứa ít nhất 8 ký tự")
    private String password;  // Mật khẩu

    @NotBlank(message = "Mật khẩu xác nhận không được để trống")
    @Size(min = 8, message = "Mật khẩu xác nhận phải chứa ít nhất 8 ký tự")
    private String confirmPassword;  // Xác nhận mật khẩu


    @NotBlank(message = "Token device không được để trống")
    private String tokenDevice;
}
