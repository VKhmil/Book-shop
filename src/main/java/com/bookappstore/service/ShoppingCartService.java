package com.bookappstore.service;

import com.bookappstore.dto.cart.CartItemRequestDto;
import com.bookappstore.dto.cart.CartItemUpdateDto;
import com.bookappstore.dto.cart.ShoppingCartResponseDto;
import org.springframework.transaction.annotation.Transactional;

public interface ShoppingCartService {

    ShoppingCartResponseDto getShoppingCartDtoByUserId(Long id);

    ShoppingCartResponseDto addCartItemByUserId(Long id,
                                                CartItemRequestDto cartItemRequestDto);

    @Transactional
    ShoppingCartResponseDto updateCartItem(Long id,
                                           Long cartItemId,
                                           CartItemUpdateDto cartItemUpdateDto);

    ShoppingCartResponseDto deleteCartItem(Long id, Long cartItemId);
}
