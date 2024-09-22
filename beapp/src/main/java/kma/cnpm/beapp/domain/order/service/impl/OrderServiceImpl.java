package kma.cnpm.beapp.domain.order.service.impl;

import kma.cnpm.beapp.domain.common.enumType.OrderStatus;
import kma.cnpm.beapp.domain.common.enumType.ShipmentStatus;
import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.order.dto.request.OrderRequest;
import kma.cnpm.beapp.domain.order.dto.response.OrderResponse;
import kma.cnpm.beapp.domain.order.entity.Order;
import kma.cnpm.beapp.domain.order.entity.OrderItem;
import kma.cnpm.beapp.domain.order.mapper.OrderMapper;
import kma.cnpm.beapp.domain.order.repository.OrderItemRepository;
import kma.cnpm.beapp.domain.order.repository.OrderRepository;
import kma.cnpm.beapp.domain.order.service.OrderService;
import kma.cnpm.beapp.domain.payment.service.AccountService;
import kma.cnpm.beapp.domain.product.service.ProductService;
import kma.cnpm.beapp.domain.shipment.service.ShipmentService;
import kma.cnpm.beapp.domain.user.entity.User;
import kma.cnpm.beapp.domain.user.service.AuthService;
import kma.cnpm.beapp.domain.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static kma.cnpm.beapp.domain.common.exception.AppErrorCode.ORDER_CANNOT_BE_CANCELLED;
import static kma.cnpm.beapp.domain.common.exception.AppErrorCode.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    OrderItemRepository orderItemRepository;
    OrderMapper orderMapper;
    UserService userService;
    AuthService authService;
    ProductService productService;
    ShipmentService shipmentService;
    AccountService accountService;

    @Override
    public String createOrder(OrderRequest orderRequest) {
        Order order = orderMapper.map(orderRequest);
        User user = userService.findUserById(authService.getAuthenticationName());
        BigDecimal totalAmount = new BigDecimal(BigInteger.ZERO);
        for (OrderItem orderItem : order.getOrderItems()) {
            // logic trừ số lượng product
            BigDecimal price = productService.reduceProductQuantity(orderItem.getProductId(), orderItem.getQuantity(), false).getPrice();
            totalAmount = totalAmount.add(price.multiply(new BigDecimal(orderItem.getQuantity())));
            orderItemRepository.save(orderItem);
        }
        order.setBuyerId(user.getId());
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.READY_FOR_DELIVERY);

        // logic trừ tiền
        accountService.payOrder(order.getId(), order.getTotalAmount(), false);

        // tạo yêu cầu giao hàng
        order.setShipmentId(shipmentService.createShipment(orderRequest.getShipmentRequest()));

        // tạo thông báo cho cả seller và buyer

        orderRepository.save(order);
        return order.getId().toString();
    }

    @Override
    public String updateOrderStatus(Long id, OrderStatus orderStatus) {

        return null;
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(AppErrorCode.ORDER_NOT_EXISTED));
        User user = userService.findUserById(authService.getAuthenticationName());
        if (!user.getId().equals(order.getBuyerId()))
            throw new AppException(UNAUTHORIZED);
        if (!order.getStatus().equals(OrderStatus.READY_FOR_DELIVERY))
            throw new AppException(ORDER_CANNOT_BE_CANCELLED);
        for (OrderItem orderItem : order.getOrderItems()) {
            // logic trừ số lượng product
            productService.reduceProductQuantity(orderItem.getProductId(), orderItem.getQuantity(), true);
        }
        accountService.payOrder(order.getId(), order.getTotalAmount(), true);
        // chinh sua status shipment
        shipmentService.updateShipmentStatus(order.getShipmentId(), ShipmentStatus.CANCELLED);
        order.setStatus(OrderStatus.CANCELED);
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        return null;
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return null;
    }

    @Override
    public List<OrderResponse> getOrderByBuyerId() {
        return null;
    }

    @Override
    public List<OrderResponse> getOrderByStatus(OrderStatus orderStatus) {
        return null;
    }
}
