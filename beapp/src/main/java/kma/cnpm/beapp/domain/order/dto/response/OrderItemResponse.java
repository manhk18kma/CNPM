package kma.cnpm.beapp.domain.order.dto.response;

import kma.cnpm.beapp.domain.common.dto.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {

    private Long id;
    private Integer quantity;
    private ProductResponse productResponse;

}
