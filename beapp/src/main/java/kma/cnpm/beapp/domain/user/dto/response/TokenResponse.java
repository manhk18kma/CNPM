package kma.cnpm.beapp.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;



import lombok.Getter;

@Getter
@Builder
public class TokenResponse {
    private Long userId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String accessToken;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String refreshToken;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String resetPasswordToken;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String activateToken;
}
