package kma.cnpm.beapp.domain.common.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kma.cnpm.beapp.domain.common.enumType.ShipmentStatus;
import kma.cnpm.beapp.domain.shipment.entity.ShipmentHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentResponse {

    private Long id;

    private Long shipperId;

    private String address;

    private LocalDateTime estimatedDeliveryDate;

    private Set<ShipmentHistory> shipmentHistories;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

}
