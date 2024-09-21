package kma.cnpm.beapp.domain.order.entity;

import jakarta.persistence.*;
import kma.cnpm.beapp.domain.common.enumType.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order extends AbstractEntity<Long> {

    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;
    private Long buyerId;
    private BigDecimal totalAmount;
    private Integer shippingAddressId;
    private Long shipperId;

}
