package kma.cnpm.beapp.domain.order.service;

import kma.cnpm.beapp.domain.common.enumType.OrderStatus;
import kma.cnpm.beapp.domain.order.dto.request.OrderRequest;
import kma.cnpm.beapp.domain.order.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    String createOrder(OrderRequest orderRequest);
    String acceptShipment(Long id);
    String completeOrder(Long id);
    String deleteOrder(Long id);

    OrderResponse getOrderById(Long id);
    List<OrderResponse> getAllOrders();
    List<OrderResponse> getOrderByBuyerId();
    List<OrderResponse> getOrderByStatus(OrderStatus orderStatus);
    List<OrderResponse> getOrderByBuyerIdAndStatus(OrderStatus orderStatus);

}
