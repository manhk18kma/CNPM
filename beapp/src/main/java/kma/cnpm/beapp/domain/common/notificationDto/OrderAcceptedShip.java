package kma.cnpm.beapp.domain.common.notificationDto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Builder
@Data
public class OrderAcceptedShip {
    Long buyerId;
    Long sellerId;
    Long orderId;
    String orderImg;


}
