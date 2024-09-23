package kma.cnpm.beapp.domain.shipment.service;

import kma.cnpm.beapp.domain.common.dto.ShipmentRequest;
import kma.cnpm.beapp.domain.common.dto.ShipmentResponse;
import kma.cnpm.beapp.domain.common.enumType.OrderStatus;

import java.util.List;

public interface ShipmentService {

    Long createShipment(ShipmentRequest shipmentRequest);
    void updateShipperId(Long id);

    ShipmentResponse getShipmentById(Long id);
    List<ShipmentResponse> getShipmentByOrderStatus(OrderStatus orderStatus);

}
