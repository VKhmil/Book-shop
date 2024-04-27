package com.bookappstore.service;

import com.bookappstore.dto.cart.CartItemRequestDto;
import com.bookappstore.dto.cart.CartItemUpdateDto;
import com.bookappstore.dto.cart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCart(Long id);

    ShoppingCartResponseDto addCartItem(
            Long userId,
            CartItemRequestDto cartItemRequestDto);

    ShoppingCartResponseDto updateCartItem(Long id,
                                           Long cartItemId,
                                           CartItemUpdateDto cartItemUpdateDto);

    ShoppingCartResponseDto deleteCartItem(Long id, Long cartItemId);
}
