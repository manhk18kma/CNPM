package kma.cnpm.beapp.domain.user.dto.resquest;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResetPasswordRequest {
    public String secretKey;
    public String password;
    public String confirmPassword;
}
