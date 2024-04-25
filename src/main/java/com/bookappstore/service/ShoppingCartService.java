package com.bookappstore.service;

import com.bookappstore.dto.cart.ShoppingCartResponseDto;

public interface ShoppingCartService {

    ShoppingCartResponseDto getShoppingCart(String email);

}
