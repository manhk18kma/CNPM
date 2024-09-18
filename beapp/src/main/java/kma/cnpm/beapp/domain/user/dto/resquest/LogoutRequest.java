package kma.cnpm.beapp.domain.user.dto.resquest;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LogoutRequest {
    @NotBlank(message = "Refresh token không được để trống")
    private String refreshToken;

    @NotBlank(message = "Access token không được để trống")
    private String accessToken;
}
