package kma.cnpm.beapp.domain.order.service.impl;

import kma.cnpm.beapp.domain.common.enumType.OrderStatus;
import kma.cnpm.beapp.domain.order.dto.request.OrderRequest;
import kma.cnpm.beapp.domain.order.dto.response.OrderResponse;
import kma.cnpm.beapp.domain.order.entity.Order;
import kma.cnpm.beapp.domain.order.entity.OrderItem;
import kma.cnpm.beapp.domain.order.mapper.OrderMapper;
import kma.cnpm.beapp.domain.order.repository.OrderItemRepository;
import kma.cnpm.beapp.domain.order.repository.OrderRepository;
import kma.cnpm.beapp.domain.order.service.OrderService;
import kma.cnpm.beapp.domain.payment.entity.Account;
import kma.cnpm.beapp.domain.product.service.ProductService;
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

    @Override
    public String createOrder(OrderRequest orderRequest) {
//        Order order = orderMapper.map(orderRequest);
//        User user = userService.findUserById(authService.getAuthenticationName());
//        BigDecimal totalAmount = new BigDecimal(BigInteger.ZERO);
//        for (OrderItem orderItem : order.getOrderItems()) {
//        // logic trừ số lượng product
//            orderItemRepository.save(orderItem);
//            totalAmount = totalAmount.add(productService.getProductById(orderItem.getProductId()).getPrice()
//                    .multiply(new BigDecimal(orderItem.getQuantity())));
//        }
//        order.setBuyerId(user.getId());
//        order.setTotalAmount(totalAmount);
//        order.setStatus(OrderStatus.READY_FOR_DELIVERY);
//
//        // logic trừ tiền
//        // nếu số tiền trong tài khoản bé hơn totalAmount => fail
//
//        // tạo yêu cầu giao hàng
//        // tạo thông báo cho cả seller và buyer
//
//
//        orderRepository.save(order);
//        return order.getId().toString();
        return null;
    }

    @Override
    public String updateOrder(Long id, OrderRequest orderRequest) {
        return null;
    }

    @Override
    public void deleteOrder(Long id) {

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
