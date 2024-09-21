package kma.cnpm.beapp.domain.order.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_items")
public class CartItem extends AbstractEntity<Long> {

    private Integer quantity;

    private Integer productId;

    private Long buyerId;

}
