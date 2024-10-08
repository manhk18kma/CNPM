package kma.cnpm.beapp.domain.common.notificationDto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Builder
@Data
public class OrderCreated {
    Long buyerId;
    Long sellerId;
    String orderId;
    BigDecimal amount;
    String orderImg;
}
