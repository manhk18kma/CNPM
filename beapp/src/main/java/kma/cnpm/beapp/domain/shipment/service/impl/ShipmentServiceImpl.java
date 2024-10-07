package kma.cnpm.beapp.domain.shipment.service.impl;

import kma.cnpm.beapp.domain.common.dto.ShipmentRequest;
import kma.cnpm.beapp.domain.common.dto.ShipmentResponse;
import kma.cnpm.beapp.domain.common.enumType.OrderStatus;
import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.common.notificationDto.ShipmentCreated;
import kma.cnpm.beapp.domain.notification.service.NotificationService;
import kma.cnpm.beapp.domain.shipment.entity.Shipment;
import kma.cnpm.beapp.domain.shipment.mapper.ShipmentMapper;
import kma.cnpm.beapp.domain.shipment.repository.ShipmentRepository;
import kma.cnpm.beapp.domain.shipment.service.ShipmentService;
import kma.cnpm.beapp.domain.user.entity.User;
import kma.cnpm.beapp.domain.user.service.AddressService;
import kma.cnpm.beapp.domain.user.service.AuthService;
import kma.cnpm.beapp.domain.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShipmentServiceImpl implements ShipmentService {

    ShipmentRepository shipmentRepository;
    ShipmentMapper shipmentMapper;
    AddressService addressService;
    UserService userService;
    AuthService authService;
    NotificationService notificationService;

    @Override
    public Long createShipment(ShipmentRequest shipmentRequest) {
        Shipment shipment = shipmentMapper.map(shipmentRequest);
        String address = addressService.getAddressById(shipmentRequest.getAddressId());
        shipment.setAddress(address);
        shipment.setEstimatedDeliveryDate(LocalDateTime.now().plusDays(5));
        shipmentRepository.save(shipment);
        // gửi thông báo cho shipper
        return shipment.getId();
    }

    @Override
    public void updateShipperId(Long id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new AppException(AppErrorCode.SHIPMENT_NOT_EXISTED));
        User user = userService.findUserById(authService.getAuthenticationName());
        shipment.setShipperId(user.getId());
        shipmentRepository.save(shipment);
        notificationService.shipmentCreated(ShipmentCreated.builder()
                .orderId(shipment.getOrderId())
                .shipmentImg(null)
                .shipmentId(shipment.getShipperId())
                .build());
    }

    @Override
    public ShipmentResponse getShipmentById(Long id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new AppException(AppErrorCode.SHIPMENT_NOT_EXISTED));
        return shipmentMapper.map(shipment);
    }

    @Override
    public List<ShipmentResponse> getShipmentByOrderStatus(OrderStatus orderStatus) {
        List<Shipment> shipments = shipmentRepository.getAllShipmentByOrderStatus(orderStatus);
        return shipments.stream()
                .map(shipmentMapper::map)
                .toList();
    }
}
