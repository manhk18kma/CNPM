package kma.cnpm.beapp.domain.common.notificationDto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderCancelled {
    Long orderId;
    Long sellerId;
    Long BuyerId;
    BigDecimal totalAmount;
    String orderImg;

}
