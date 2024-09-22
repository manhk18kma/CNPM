package kma.cnpm.beapp.domain.shipment.service;

import kma.cnpm.beapp.domain.common.dto.ShipmentRequest;
import kma.cnpm.beapp.domain.common.enumType.ShipmentStatus;

public interface ShipmentService {

    Long createShipment(ShipmentRequest shipmentRequest);
    Long updateShipmentStatus(Long id, ShipmentStatus shipmentStatus);

}
