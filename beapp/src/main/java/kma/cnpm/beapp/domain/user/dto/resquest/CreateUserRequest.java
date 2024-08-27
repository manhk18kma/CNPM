package kma.cnpm.beapp.domain.user.dto.resquest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import jakarta.validation.constraints.Size;
import kma.cnpm.beapp.domain.common.validation.PhoneNumber;
import lombok.Getter;

import java.util.Date;

@Getter
public class CreateUserRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String confirmPassword;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 50, message = "Full name must be between 2 and 50 characters")
    private String fullName;

//    @PhoneNumber(message = "Phone number must be in a valid format")
//    private String phone;

    @NotBlank(message = "captchaToken is required")
    private String captchaToken;

}
