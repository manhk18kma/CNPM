package kma.cnpm.beapp.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Builder
@Data // Tạo getter, setter, toString, equals, hashcod
public class FollowResponse {
    private Long userId;
    private Long followId;
    private String fullName;
    private String avatar;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long followedUserId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long followerUserId;

}
