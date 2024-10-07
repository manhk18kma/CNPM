package kma.cnpm.beapp.domain.shipment.repository;

import kma.cnpm.beapp.domain.common.enumType.OrderStatus;
import kma.cnpm.beapp.domain.shipment.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    @Query("SELECT s FROM Shipment s JOIN Order o ON s.orderId = o.id WHERE o.status = :status")
    List<Shipment> getAllShipmentByOrderStatus(@Param("status") OrderStatus status);
}
