package kma.cnpm.beapp.domain.user.dto.resquest;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ForgotPassRequest {
    @NotBlank(message = "Email không được để trống")
    private String email;
}
