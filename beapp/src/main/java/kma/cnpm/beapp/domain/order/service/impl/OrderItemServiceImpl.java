package kma.cnpm.beapp.domain.order.service.impl;

import kma.cnpm.beapp.domain.order.dto.request.OrderItemRequest;
import kma.cnpm.beapp.domain.order.dto.response.OrderItemResponse;
import kma.cnpm.beapp.domain.order.mapper.OrderItemMapper;
import kma.cnpm.beapp.domain.order.repository.OrderItemRepository;
import kma.cnpm.beapp.domain.order.service.OrderItemService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderItemServiceImpl implements OrderItemService {

    OrderItemRepository orderItemRepository;
    OrderItemMapper orderItemMapper;


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
}
