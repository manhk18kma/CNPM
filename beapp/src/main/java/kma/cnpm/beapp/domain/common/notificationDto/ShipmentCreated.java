package kma.cnpm.beapp.domain.common.notificationDto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ShipmentCreated {
    String orderId;
    Long shipmentId;
    String shipmentImg;

}
