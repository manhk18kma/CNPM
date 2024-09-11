package kma.cnpm.beapp.domain.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data // Tạo getter, setter, toString, equals, hashcod
public class FollowResponse {
    private Long followId;
    private Long userId;
    private String fullName;
    private String avatar;
}
