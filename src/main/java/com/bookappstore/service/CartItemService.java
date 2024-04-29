package com.bookappstore.service;

import com.bookappstore.dto.cart.CartItemRequestDto;
import com.bookappstore.dto.cart.CartItemResponseDto;
import com.bookappstore.dto.cart.ShoppingCartDto;

public interface CartItemService {
    CartItemResponseDto saveItemToShoppingCart(CartItemRequestDto requestDto,
                                               ShoppingCartDto responseDto);

    CartItemResponseDto getById(Long id);

    CartItemResponseDto delete(Long id);

    void addQuantity(Long id, int quantity);
}
