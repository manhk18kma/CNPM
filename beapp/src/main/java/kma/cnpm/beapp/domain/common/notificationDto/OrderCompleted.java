package kma.cnpm.beapp.domain.common.notificationDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderCompleted {
    String orderId;
    Long sellerId;
    Long buyerId;
    String orderImg;

}
