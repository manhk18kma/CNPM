package kma.cnpm.beapp.domain.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentRequest {

    private Long shipperId;

    private Long orderId;

    private Long addressId;


}
