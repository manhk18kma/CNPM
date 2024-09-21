package kma.cnpm.beapp.domain.order.service.impl;

import kma.cnpm.beapp.domain.common.dto.ProductResponse;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.order.dto.request.CartItemRequest;
import kma.cnpm.beapp.domain.order.dto.response.CartItemResponse;
import kma.cnpm.beapp.domain.order.entity.CartItem;
import kma.cnpm.beapp.domain.order.mapper.CartItemMapper;
import kma.cnpm.beapp.domain.order.repository.CartRepository;
import kma.cnpm.beapp.domain.order.service.CartService;
import kma.cnpm.beapp.domain.product.service.ProductService;
import kma.cnpm.beapp.domain.user.entity.User;
import kma.cnpm.beapp.domain.user.service.AuthService;
import kma.cnpm.beapp.domain.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kma.cnpm.beapp.domain.common.exception.AppErrorCode.CART_ITEM_NOT_EXISTED;
import static kma.cnpm.beapp.domain.common.exception.AppErrorCode.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {

    CartRepository cartRepository;
    CartItemMapper cartItemMapper;
    UserService userService;
    AuthService authService;
    ProductService productService;

    @Override
    public void addCartItem(CartItemRequest cartItemRequest) {
        User user = userService.findUserById(authService.getAuthenticationName());
        CartItem cartItem = cartItemMapper.map(cartItemRequest);
        cartItem.setBuyerId(user.getId());
        cartRepository.save(cartItem);
    }

    @Override
    public void removeCartItem(Long id) {
        CartItem cartItem = cartRepository.findById(id)
                .orElseThrow(() -> new AppException(CART_ITEM_NOT_EXISTED));
        User user = userService.findUserById(authService.getAuthenticationName());
        if (!user.getId().equals(cartItem.getBuyerId()))
            throw new AppException(UNAUTHORIZED);
        cartRepository.deleteById(id);
    }

    @Override
    public CartItemResponse getCartById(Long id) {
        CartItem cartItem = cartRepository.findById(id)
                .orElseThrow(() -> new AppException(CART_ITEM_NOT_EXISTED));
        CartItemResponse cartItemResponse = cartItemMapper.map(cartItem);
        ProductResponse productResponse = productService.getProductById(cartItem.getProductId());
        cartItemResponse.setProductResponse(productResponse);
        return cartItemResponse;
    }

    @Override
    public List<CartItemResponse> getCartByBuyerId(Long buyerId) {
        List<CartItem> cartItems = cartRepository.findCartItemByBuyerId(buyerId);
        return cartItems.stream()
                .map(cartItemMapper::map)
                .peek(cartItemResponse -> cartItemResponse.setProductResponse(
                        productService.getProductById(cartItemResponse.getProductResponse().getId())))
                .toList();
    }

    @Override
    public List<CartItemResponse> getAllCartItem() {
        List<CartItem> cartItems = cartRepository.findAll();
        return cartItems.stream()
                .map(cartItemMapper::map)
                .peek(cartItemResponse -> cartItemResponse.setProductResponse(
                        productService.getProductById(cartItemResponse.getProductResponse().getId())))
                .toList();
    }

}
