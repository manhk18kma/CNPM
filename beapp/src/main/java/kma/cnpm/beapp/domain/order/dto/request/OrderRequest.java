package kma.cnpm.beapp.domain.order.dto.request;

import kma.cnpm.beapp.domain.common.dto.ShipmentRequest;
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

    private List<OrderItemRequest> orderItemRequests;
    private ShipmentRequest shipmentRequest;

}
