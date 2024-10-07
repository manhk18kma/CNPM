package kma.cnpm.beapp.domain.shipment.mapper;

import kma.cnpm.beapp.domain.common.dto.ShipmentRequest;
import kma.cnpm.beapp.domain.common.dto.ShipmentResponse;
import kma.cnpm.beapp.domain.shipment.entity.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {

    Shipment map(ShipmentRequest shipmentRequest);
    ShipmentResponse map(Shipment shipment);

}
