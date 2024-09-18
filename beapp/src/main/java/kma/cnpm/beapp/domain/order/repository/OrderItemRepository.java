package kma.cnpm.beapp.domain.order.repository;

import kma.cnpm.beapp.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
