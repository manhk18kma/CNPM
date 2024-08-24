package kma.cnwat.be.domain.user.dto.resquest;

import lombok.Getter;

@Getter
public class RefreshTokenRequest {
    private  String refreshToken;
    private  String accessToken;
}
