package kma.cnpm.beapp.domain.order.entity;

import jakarta.persistence.*;
import kma.cnpm.beapp.domain.common.enumType.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    private Long buyerId;
    private BigDecimal totalAmount;
    private Long shipmentId;

    @CreationTimestamp
    private LocalDateTime orderedDate;

    @UpdateTimestamp
    private LocalDateTime endDate;

}
