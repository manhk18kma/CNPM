package kma.cnpm.beapp.domain.order.mapper;

import kma.cnpm.beapp.domain.order.dto.request.OrderItemRequest;
import kma.cnpm.beapp.domain.order.dto.response.OrderItemResponse;
import kma.cnpm.beapp.domain.order.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItem map(OrderItemRequest orderItemRequest);

    @Mapping(target = "productResponse.id", source = "productId")
    OrderItemResponse map(OrderItem orderItem);

}
