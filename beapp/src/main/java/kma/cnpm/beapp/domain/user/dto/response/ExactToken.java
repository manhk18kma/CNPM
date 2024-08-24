package kma.cnpm.beapp.domain.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ExactToken {
    String username;
    String token;
    String jwtId;
}
