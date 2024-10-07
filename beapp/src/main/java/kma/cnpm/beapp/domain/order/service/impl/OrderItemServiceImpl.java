package kma.cnpm.beapp.domain.order.service.impl;

import kma.cnpm.beapp.domain.common.enumType.OrderStatus;
import kma.cnpm.beapp.domain.order.dto.request.OrderItemRequest;
import kma.cnpm.beapp.domain.order.dto.response.OrderItemResponse;
import kma.cnpm.beapp.domain.order.entity.OrderItem;
import kma.cnpm.beapp.domain.order.mapper.OrderItemMapper;
import kma.cnpm.beapp.domain.order.repository.OrderItemRepository;
import kma.cnpm.beapp.domain.order.service.OrderItemService;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderItemServiceImpl implements OrderItemService {

    OrderItemRepository orderItemRepository;
    OrderItemMapper orderItemMapper;
    UserService userService;
    AuthService authService;
    ProductService productService;


    @Override
    public void addOrderItem(OrderItemRequest orderItemRequest) {

    }

    @Override
    public void removeOrderItem(Long id) {

    }

    @Override
    public OrderItemResponse getOrderItemById(Long id) {
        return null;
    }

    @Override
    public List<OrderItemResponse> getOrderItemsByProductId(Integer productId) {
        return null;
    }

    @Override
    public List<OrderItemResponse> getOrderItemsByOrderStatusAndUser(OrderStatus orderStatus) {
        User user = userService.findUserById(authService.getAuthenticationName());
        List<OrderItem> orderItems = orderItemRepository.findAllByOrder_StatusAndOrder_BuyerId(orderStatus, user.getId());
        return orderItems.stream()
                .map(orderItemMapper::map)
                .peek(orderItemResponse -> {
                    orderItemResponse.setProductName(
                            productService.getProductById(orderItemResponse.getProductId()).getName());
                    orderItemResponse.setProductPrice(
                            productService.getProductById(orderItemResponse.getProductId()).getPrice());
                })
                .toList();
    }

}
