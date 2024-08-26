package kma.cnpm.beapp.domain.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String categoryName;

    private Long sellerId;
    private String sellerName;

}
