package kma.cnpm.beapp.domain.order.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
    private Long buyerId;
    private BigDecimal totalAmount;
    private Long paymentId;
    private Integer shippingAddressId;
    private Long shipperId;
    private LocalDateTime orderedDate;
    private LocalDateTime endDate;

}
