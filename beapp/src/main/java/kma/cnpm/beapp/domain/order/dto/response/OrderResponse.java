package kma.cnpm.beapp.domain.order.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import kma.cnpm.beapp.domain.common.dto.ShipmentResponse;
import kma.cnpm.beapp.domain.common.enumType.OrderStatus;
import kma.cnpm.beapp.domain.order.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private OrderStatus status;

    @JsonProperty("orderItem")
    private List<OrderItemResponse> orderItemResponses;
    private Long buyerId;
    private BigDecimal totalAmount;

    @JsonProperty("shipment")
    private ShipmentResponse shipmentResponse;

    private LocalDateTime orderedDate;
    private LocalDateTime endDate;

}
