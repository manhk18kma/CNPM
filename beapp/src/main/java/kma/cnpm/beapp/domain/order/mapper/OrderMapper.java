package kma.cnpm.beapp.domain.order.mapper;


import kma.cnpm.beapp.domain.order.dto.request.OrderRequest;
import kma.cnpm.beapp.domain.order.dto.response.OrderResponse;
import kma.cnpm.beapp.domain.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface OrderMapper {

    @Mapping(target = "orderItemResponses", source = "orderItems")
    @Mapping(target = "shipmentResponse.id", source = "shipmentId")
    OrderResponse map(Order order);

    @Mapping(target = "orderItems", source = "orderItemRequests")
    Order map(OrderRequest orderRequest);

}
