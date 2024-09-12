package kma.cnpm.beapp.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long userId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long userTargetId; //for relation follow
}
