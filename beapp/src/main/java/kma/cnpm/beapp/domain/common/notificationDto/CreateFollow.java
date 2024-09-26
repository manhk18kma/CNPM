package kma.cnpm.beapp.domain.common.notificationDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CreateFollow {
    String followerFullName;
    String followerAvt;
    Long followerId;
    Long followedId;
}
