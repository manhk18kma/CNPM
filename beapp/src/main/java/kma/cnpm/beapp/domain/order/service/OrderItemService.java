package kma.cnpm.beapp.domain.order.service;

import kma.cnpm.beapp.domain.order.dto.request.OrderItemRequest;
import kma.cnpm.beapp.domain.order.dto.response.OrderItemResponse;

import java.util.List;

public interface OrderItemService {

    void addOrderItem(OrderItemRequest orderItemRequest);
    void removeOrderItem(Long id);

    OrderItemResponse getOrderItemById(Long id);
    List<OrderItemResponse> getOrderItemsByProductId(Integer productId);

}
