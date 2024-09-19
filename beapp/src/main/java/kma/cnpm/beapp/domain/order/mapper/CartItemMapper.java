package kma.cnpm.beapp.domain.order.mapper;

import kma.cnpm.beapp.domain.order.dto.request.CartItemRequest;
import kma.cnpm.beapp.domain.order.dto.response.CartItemResponse;
import kma.cnpm.beapp.domain.order.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    CartItem map(CartItemRequest cartItemRequest);

    @Mapping(target = "productResponse.id", source = "productId")
    CartItemResponse map(CartItem cartItem);

}
