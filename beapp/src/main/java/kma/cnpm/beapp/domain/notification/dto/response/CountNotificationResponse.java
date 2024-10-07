package kma.cnpm.beapp.domain.notification.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CountNotificationResponse {
    private int totalNotRead;

}
