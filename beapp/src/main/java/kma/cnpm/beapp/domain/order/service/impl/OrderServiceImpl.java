package kma.cnpm.beapp.domain.order.service.impl;

import kma.cnpm.beapp.domain.common.dto.ProductResponse;
import kma.cnpm.beapp.domain.common.dto.ShipmentRequest;
import kma.cnpm.beapp.domain.common.enumType.OrderStatus;
import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.common.notificationDto.OrderAcceptedShip;
import kma.cnpm.beapp.domain.common.notificationDto.OrderCancelled;
import kma.cnpm.beapp.domain.common.notificationDto.OrderCompleted;
import kma.cnpm.beapp.domain.common.notificationDto.OrderCreated;
import kma.cnpm.beapp.domain.notification.service.NotificationService;
import kma.cnpm.beapp.domain.order.dto.request.OrderRequest;
import kma.cnpm.beapp.domain.order.dto.response.CartItemResponse;
import kma.cnpm.beapp.domain.order.dto.response.OrderItemResponse;
import kma.cnpm.beapp.domain.order.dto.response.OrderResponse;
import kma.cnpm.beapp.domain.order.entity.Order;
import kma.cnpm.beapp.domain.order.entity.OrderItem;
import kma.cnpm.beapp.domain.order.mapper.OrderMapper;
import kma.cnpm.beapp.domain.order.repository.OrderRepository;
import kma.cnpm.beapp.domain.order.service.CartService;
import kma.cnpm.beapp.domain.order.service.OrderService;
import kma.cnpm.beapp.domain.payment.entity.Account;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static kma.cnpm.beapp.domain.common.exception.AppErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    OrderMapper orderMapper;
    UserService userService;
    AuthService authService;
    CartService cartService;
    ProductService productService;
    ShipmentService shipmentService;
    AccountService accountService;
    NotificationService notificationService;

    @Override
    public String createOrderByCart(OrderRequest orderRequest) {
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        User user = userService.findUserById(authService.getAuthenticationName());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = new BigDecimal(BigInteger.ZERO);
        for (CartItemResponse cartItemResponse : cartService.getCartByBuyerId(user.getId())) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setQuantity(cartItemResponse.getQuantity());
            orderItem.setProductId(cartItemResponse.getProductResponse().getId());
            cartService.removeCartItem(cartItemResponse.getId());
            // trừ số lượng product trong kho
            BigDecimal price = productService.reduceProductQuantity(orderItem.getProductId(), orderItem.getQuantity(), false).getPrice();
            totalAmount = totalAmount.add(price.multiply(new BigDecimal(orderItem.getQuantity())));
        }
        order.setOrderItems(orderItems);
        order.setBuyerId(user.getId());
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.READY_FOR_DELIVERY);
//        orderItemRepository.saveAll(order.getOrderItems());
        // tạo yêu cầu giao hàng
        order.setShipmentId(shipmentService.createShipment(
                ShipmentRequest.builder()
                        .orderId(order.getId())
                        .shipperId(null)
                        .addressId(orderRequest.getAddressId())
                        .build()));

        // thanh toán
        accountService.payOrder(order.getTotalAmount(), false);

        // tạo thông báo cho cả seller và buyer
        //

        orderRepository.save(order);
        return order.getId();
    }

    @Override
    public String createOrder(OrderRequest orderRequest) {
        Order order = orderMapper.map(orderRequest);
        order.setId(UUID.randomUUID().toString());

        User user = userService.findUserById(authService.getAuthenticationName());
        order.setBuyerId(user.getId());

        OrderItem orderItem = order.getOrderItems().get(0);
        orderItem.setOrder(order);
        order.setOrderItems(new ArrayList<>(Collections.singleton(orderItem)));

        // trừ số lượng product trong kho
        BigDecimal price = productService.reduceProductQuantity(orderItem.getProductId(), orderItem.getQuantity(), false).getPrice();
        BigDecimal totalAmount = price.multiply(new BigDecimal(orderItem.getQuantity()));
        order.setTotalAmount(totalAmount);

        // tạo yêu cầu giao hàng
        order.setShipmentId(shipmentService.createShipment(
                ShipmentRequest.builder()
                        .orderId(order.getId())
                        .shipperId(null)
                        .addressId(orderRequest.getAddressId())
                        .build()));

        // thanh toán
        accountService.payOrder(order.getTotalAmount(), false);

        // tạo thông báo cho cả seller và buyer
        ProductResponse productResponse = productService.getProductById(orderItem.getProductId());
        notificationService.orderCreated(OrderCreated.builder()
                .orderId(order.getId())
                .amount(totalAmount)
                .sellerId(productResponse.getSellerId())
                .buyerId(order.getBuyerId())
                .orderImg(productResponse.getMediaResponses().get(0).getUrl())
                .build());

        order.setStatus(OrderStatus.READY_FOR_DELIVERY);
        orderRepository.save(order);
        return order.getId();
    }

    @Override
    public String acceptShipment(String id) {
        // role shipper
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(AppErrorCode.ORDER_NOT_EXISTED));
        if (!order.getStatus().equals(OrderStatus.READY_FOR_DELIVERY))
            throw new AppException(ORDER_CANNOT_BE_ACCEPT);
        shipmentService.updateShipperId(order.getShipmentId());
        order.setStatus(OrderStatus.IN_TRANSIT);
        orderRepository.save(order);
        notificationService.orderAcceptShip(OrderAcceptedShip.builder()
                .orderId(order.getId())
                .orderImg(null)
                .buyerId(order.getBuyerId())
                .sellerId(productService.getProductById(order.getOrderItems().get(0).getProductId()).getSellerId())
                .build());
        return order.getId();
    }

    @Override
    public String completeOrder(String id) {
        // role shipper
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ORDER_NOT_EXISTED));
        User user = userService.findUserById(authService.getAuthenticationName());
        if (!user.getId().equals(shipmentService.getShipmentById(order.getShipmentId()).getShipperId()))
            throw new AppException(UNAUTHORIZED);
        if (!order.getStatus().equals(OrderStatus.IN_TRANSIT))
            throw new AppException(ORDER_CANNOT_BE_COMPLETE);
        order.getOrderItems()
                .forEach(orderItem -> {
                    ProductResponse productResponse = productService.getProductById(orderItem.getProductId());
                    Account account = accountService.getAccountByUserId(productResponse.getSellerId());
                    account.setBalance(account.getBalance().add(productResponse.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))));
                });
        order.setStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);
        notificationService.orderComplete(OrderCompleted.builder()
                .orderId(order.getId())
                .orderImg(null)
                .BuyerId(order.getBuyerId())
                .sellerId(productService.getProductById(order.getOrderItems().get(0).getProductId()).getSellerId())
                .build());
        return order.getId();
    }

    @Override
    public String deleteOrder(String id) {
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
        // hoan tien
        accountService.payOrder(order.getTotalAmount(), true);
        // chinh sua status shipment
        order.setStatus(OrderStatus.CANCELED);
        notificationService.orderCancelled(OrderCancelled.builder()
                .totalAmount(order.getTotalAmount())
                .orderId(order.getId())
                .orderImg(null)
                .BuyerId(order.getBuyerId())
                .sellerId(productService.getProductById(order.getOrderItems().get(0).getProductId()).getSellerId())
                .build());
        return order.getId();
    }

    @Override
    public OrderResponse getOrderById(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ORDER_NOT_EXISTED));
        OrderResponse orderResponse = orderMapper.map(order);
        orderResponse.setShipmentResponse(shipmentService.getShipmentById(order.getShipmentId()));
        List<OrderItemResponse> orderItemResponses = orderResponse.getOrderItemResponses()
                .stream()
                .peek(orderItemResponse -> {
                    orderItemResponse.setProductName(productService.getProductById(orderItemResponse.getProductId()).getName());
                    orderItemResponse.setProductPrice(productService.getProductById(orderItemResponse.getProductId()).getPrice());
                })
                .toList();
        orderResponse.setOrderItemResponses(orderItemResponses);
        return orderResponse;
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::map)
                .peek(orderResponse -> {
                    orderResponse.setShipmentResponse(shipmentService.getShipmentById(orderResponse.getShipmentResponse().getId()));
                    List<OrderItemResponse> orderItemResponses = orderResponse.getOrderItemResponses()
                            .stream()
                            .peek(orderItemResponse -> {
                                orderItemResponse.setProductName(productService.getProductById(orderItemResponse.getProductId()).getName());
                                orderItemResponse.setProductPrice(productService.getProductById(orderItemResponse.getProductId()).getPrice());
                            })
                            .toList();
                    orderResponse.setOrderItemResponses(orderItemResponses);
                })
                .toList();
    }

    @Override
    public List<OrderResponse> getOrderByBuyerId() {
        User user = userService.findUserById(authService.getAuthenticationName());
        List<Order> orders = orderRepository.findAllByBuyerId(user.getId());
        return orders.stream()
                .map(orderMapper::map)
                .peek(orderResponse -> {
                    orderResponse.setShipmentResponse(shipmentService.getShipmentById(orderResponse.getShipmentResponse().getId()));
                    List<OrderItemResponse> orderItemResponses = orderResponse.getOrderItemResponses()
                            .stream()
                            .peek(orderItemResponse -> {
                                orderItemResponse.setProductName(productService.getProductById(orderItemResponse.getProductId()).getName());
                                orderItemResponse.setProductPrice(productService.getProductById(orderItemResponse.getProductId()).getPrice());
                            })
                            .toList();
                    orderResponse.setOrderItemResponses(orderItemResponses);
                })
                .toList();
    }

    @Override
    public List<OrderResponse> getOrderByStatus(OrderStatus orderStatus) {
        List<Order> orders = orderRepository.findAllByStatus(orderStatus);
        return orders.stream()
                .map(orderMapper::map)
                .peek(orderResponse -> {
                    orderResponse.setShipmentResponse(shipmentService.getShipmentById(orderResponse.getShipmentResponse().getId()));
                    List<OrderItemResponse> orderItemResponses = orderResponse.getOrderItemResponses()
                            .stream()
                            .peek(orderItemResponse -> {
                                orderItemResponse.setProductName(productService.getProductById(orderItemResponse.getProductId()).getName());
                                orderItemResponse.setProductPrice(productService.getProductById(orderItemResponse.getProductId()).getPrice());
                            })
                            .toList();
                    orderResponse.setOrderItemResponses(orderItemResponses);
                })
                .toList();
    }

    @Override
    public List<OrderResponse> getOrderByBuyerIdAndStatus(OrderStatus orderStatus) {
        User user = userService.findUserById(authService.getAuthenticationName());
        List<Order> orders = orderRepository.findAllByBuyerIdAndStatus(user.getId(), orderStatus);
        return orders.stream()
                .map(orderMapper::map)
                .peek(orderResponse -> {
                    orderResponse.setShipmentResponse(shipmentService.getShipmentById(orderResponse.getShipmentResponse().getId()));
                    List<OrderItemResponse> orderItemResponses = orderResponse.getOrderItemResponses()
                            .stream()
                            .peek(orderItemResponse -> {
                                orderItemResponse.setProductName(productService.getProductById(orderItemResponse.getProductId()).getName());
                                orderItemResponse.setProductPrice(productService.getProductById(orderItemResponse.getProductId()).getPrice());
                            })
                            .toList();
                    orderResponse.setOrderItemResponses(orderItemResponses);
                })
                .toList();
    }


}
