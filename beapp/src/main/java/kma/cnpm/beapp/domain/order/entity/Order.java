package kma.cnpm.beapp.domain.order.entity;

import jakarta.persistence.*;
import kma.cnpm.beapp.domain.common.enumType.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order extends AbstractEntity<Long> {

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;
    private Long buyerId;
    private BigDecimal totalAmount;
    private Long shipmentId;

}
