package kma.cnpm.beapp.domain.order.repository;

import kma.cnpm.beapp.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
