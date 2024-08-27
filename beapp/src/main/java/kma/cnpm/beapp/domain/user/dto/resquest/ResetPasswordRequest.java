package kma.cnpm.beapp.domain.user.dto.resquest;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResetPasswordRequest {
    private String secretKey;
    private String password;
    private String confirmPassword;
    private String tokenDevice;

}
