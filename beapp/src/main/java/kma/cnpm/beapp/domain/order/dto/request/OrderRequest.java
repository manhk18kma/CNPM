package kma.cnpm.beapp.domain.order.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @JsonProperty("orderItem")
    private List<OrderItemRequest> orderItemRequests;

    private Long addressId;

}
