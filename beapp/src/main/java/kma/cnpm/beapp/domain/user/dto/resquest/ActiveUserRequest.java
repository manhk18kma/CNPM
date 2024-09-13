package kma.cnpm.beapp.domain.user.dto.resquest;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ActiveUserRequest {

    @NotBlank(message = "Token không được để trống")
    private String token;      // Mã kích hoạt người dùng

    @NotBlank(message = "Token thiết bị không được để trống")
    private String tokenDevice; // Mã thiết bị của người dùng
}