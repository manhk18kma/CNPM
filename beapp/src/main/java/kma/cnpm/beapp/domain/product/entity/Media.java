package kma.cnpm.beapp.domain.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "media")
public class Media extends AbstractEntity<Integer> {

    private String url;

    private String type; // "IMAGE" or "VIDEO"

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
