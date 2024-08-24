package kma.cnpm.beapp.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import kma.cnwat.be.domain.common.enumType.TokenType;
import lombok.Builder;



import lombok.Getter;

@Getter
@Builder
public class TokenResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String accessToken;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String refreshToken;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String resetPasswordToken;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String activateToken;
}
