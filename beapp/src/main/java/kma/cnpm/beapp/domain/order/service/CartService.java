package kma.cnpm.beapp.domain.order.service;

import kma.cnpm.beapp.domain.order.dto.request.CartItemRequest;
import kma.cnpm.beapp.domain.order.dto.response.CartItemResponse;

import java.util.List;

public interface CartService {

    void addCartItem(CartItemRequest cartItemRequest);

    void removeCartItem(Long id);

    CartItemResponse getCartById(Long id);
    List<CartItemResponse> getCartByBuyerId(Long buyerId);
    List<CartItemResponse> getAllCartItem();

}
