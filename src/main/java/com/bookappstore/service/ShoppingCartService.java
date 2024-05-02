package com.bookappstore.service;

import com.bookappstore.dto.cart.CartItemRequestDto;
import com.bookappstore.dto.cart.CartItemUpdateDto;
import com.bookappstore.dto.cart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(Long id);

    ShoppingCartDto addCartItemToShoppingCart(Long userId,
                                              CartItemRequestDto cartItemRequestDto);

    ShoppingCartDto updateCartItem(Long userId,
                                   Long cartItemId,
                                   CartItemUpdateDto request);

    ShoppingCartDto removeCartItem(Long userId, Long cartItemId);
}
