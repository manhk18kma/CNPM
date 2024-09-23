package kma.cnpm.beapp.domain.shipment.entity;

import jakarta.persistence.*;
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

}
