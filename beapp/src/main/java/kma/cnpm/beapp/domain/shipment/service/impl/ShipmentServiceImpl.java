package kma.cnpm.beapp.domain.shipment.service.impl;

import kma.cnpm.beapp.domain.common.dto.ShipmentRequest;
import kma.cnpm.beapp.domain.common.enumType.ShipmentStatus;
import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.shipment.entity.Shipment;
import kma.cnpm.beapp.domain.shipment.mapper.ShipmentMapper;
import kma.cnpm.beapp.domain.shipment.repository.ShipmentRepository;
import kma.cnpm.beapp.domain.shipment.service.ShipmentService;
import kma.cnpm.beapp.domain.user.service.AddressService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShipmentServiceImpl implements ShipmentService {

    ShipmentRepository shipmentRepository;
    ShipmentMapper shipmentMapper;
    AddressService addressService;

    @Override
    public Long createShipment(ShipmentRequest shipmentRequest) {
        Shipment shipment = shipmentMapper.map(shipmentRequest);
        String address = addressService.getAddressById(shipmentRequest.getAddressId());
        shipment.setAddress(address);
        shipment.setStatus(ShipmentStatus.AWAITING_PICKUP);
        shipment.setEstimatedDeliveryDate(shipment.getCreatedAt().plusDays(5));
        shipmentRepository.save(shipment);
        // gửi thông báo cho shipper
        return shipment.getId();
    }

    @Override
    public Long updateShipmentStatus(Long id, ShipmentStatus shipmentStatus) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new AppException(AppErrorCode.SHIPMENT_NOT_EXISTED));
        shipment.setStatus(shipmentStatus);
        shipmentRepository.save(shipment);
        return shipment.getId();
    }
}
