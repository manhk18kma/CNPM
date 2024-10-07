package kma.cnpm.beapp.domain.shipment.entity;


import jakarta.persistence.*;
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
    private String orderId;

    @Column(name = "address")
    private String address;

    @Column(name = "estimated_delivery_date")
    private LocalDateTime estimatedDeliveryDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "shipment")
    private Set<ShipmentHistory> shipmentHistories;

}
