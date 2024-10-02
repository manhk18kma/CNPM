package kma.cnpm.beapp.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {

    private Long id;

    private Integer quantity;

    private Integer productId;

    private String productName;

    private BigDecimal productPrice;


}
