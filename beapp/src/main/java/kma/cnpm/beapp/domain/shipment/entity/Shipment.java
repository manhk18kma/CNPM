package kma.cnpm.beapp.domain.shipment.entity;


import jakarta.persistence.*;
import kma.cnpm.beapp.domain.common.enumType.ShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shipments")
public class Shipment extends AbstractEntity<Long> {

    @Column(name = "shipper_id")
    private Long shipperId;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "address")
    private String address;

    @Column(name = "estimated_delivery_date")
    private LocalDateTime estimatedDeliveryDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "shipment")
    private Set<ShipmentHistory> shipmentHistories;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false , name = "status")
    private ShipmentStatus status;

}
