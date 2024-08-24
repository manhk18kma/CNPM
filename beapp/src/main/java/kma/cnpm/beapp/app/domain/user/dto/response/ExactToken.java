package kma.cnwat.be.domain.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ExactToken {
    String username;
    String token;
    String jwtId;
}
