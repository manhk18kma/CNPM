package kma.cnpm.beapp.domain.shipment.repository;

import kma.cnpm.beapp.domain.shipment.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}
