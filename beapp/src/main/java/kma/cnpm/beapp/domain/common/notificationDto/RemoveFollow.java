package kma.cnpm.beapp.domain.common.notificationDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RemoveFollow {
    Long followerId;
    Long followedId;
}
