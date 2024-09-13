package kma.cnpm.beapp.domain.user.dto.resquest;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RefreshTokenRequest {
    @NotBlank(message = "Refresh token không được để trống")
    private  String refreshToken;
}
