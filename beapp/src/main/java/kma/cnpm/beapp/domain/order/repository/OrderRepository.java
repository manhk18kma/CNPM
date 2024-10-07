package kma.cnpm.beapp.domain.order.repository;

import kma.cnpm.beapp.domain.common.enumType.OrderStatus;
import kma.cnpm.beapp.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findAllByBuyerId(Long buyerId);
    List<Order> findAllByStatus(OrderStatus orderStatus);
    List<Order> findAllByBuyerIdAndStatus(Long buyerId, OrderStatus orderStatus);
}
