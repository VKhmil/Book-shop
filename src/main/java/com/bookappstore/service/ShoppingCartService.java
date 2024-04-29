package com.bookappstore.service;

import com.bookappstore.dto.cart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(Long id);
}
