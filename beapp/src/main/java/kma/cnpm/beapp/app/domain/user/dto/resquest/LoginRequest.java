package kma.cnwat.be.domain.user.dto.resquest;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {
    @NotBlank(message = "username must be not null")
    private String username;

    @NotBlank(message = "username must be not blank")
    private String password;

    private String deviceToken;

//    private Platform platform;
//
//    private String deviceToken;
//
//    private String version;
}
