package kma.cnpm.beapp.domain.shipment.entity;

import jakarta.persistence.*;
import kma.cnpm.beapp.domain.common.enumType.OrderStatus;
import kma.cnpm.beapp.domain.common.enumType.ShipmentStatus;
import lombok.*;

@Setter
@Getter
@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_shipment_history")
public class ShipmentHistory extends AbstractEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status; // Trạng thái của shipment (READY_FOR_DELIVERY, IN_TRANSIT, DELIVERED, CANCELED, ...)

    private String location; // Vị trí hiện tại của shipment

    private String description; // Miêu tả chi tiết về sự kiện (ví dụ: đã chuyển đến kho, đang giao, ...)


}
