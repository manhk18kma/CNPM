package kma.cnpm.beapp.domain.user.dto.resquest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import kma.cnpm.beapp.domain.common.validation.EmailValidation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Email không được để trống")
    @EmailValidation(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    @NotBlank(message = "Token device không được để trống")
    private String deviceToken;

}
