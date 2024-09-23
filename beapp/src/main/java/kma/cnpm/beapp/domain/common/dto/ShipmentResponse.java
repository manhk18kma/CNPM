package kma.cnpm.beapp.domain.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentResponse {

    private Long id;

    private Long shipperId;

    private Long orderId;

    private String fullName;

    private String phone;

    private String address;

    private LocalDateTime estimatedDeliveryDate;

}
