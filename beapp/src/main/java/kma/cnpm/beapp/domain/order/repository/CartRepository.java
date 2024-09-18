package kma.cnpm.beapp.domain.order.repository;

import kma.cnpm.beapp.domain.order.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findCartItemByBuyerId(Long buyerId);

}
