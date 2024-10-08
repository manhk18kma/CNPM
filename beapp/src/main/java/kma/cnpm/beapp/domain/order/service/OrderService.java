package kma.cnpm.beapp.domain.order.service;

import kma.cnpm.beapp.domain.common.enumType.OrderStatus;
import kma.cnpm.beapp.domain.order.dto.request.OrderRequest;
import kma.cnpm.beapp.domain.order.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    String createOrderByCart(OrderRequest orderRequest);
    String createOrder(OrderRequest orderRequest);
    String acceptShipment(String id);
    String completeOrder(String id);
    String deleteOrder(String id);
    String deleteOrderByShipper(String id);

    OrderResponse getOrderById(String id);
    List<OrderResponse> getAllOrders();
    List<OrderResponse> getOrderByBuyerId();
    List<OrderResponse> getOrderByStatus(OrderStatus orderStatus);
    List<OrderResponse> getOrderByBuyerIdAndStatus(OrderStatus orderStatus);

}
