package com.bookappstore.controller;

import com.bookappstore.dto.cart.ShoppingCartResponseDto;
import com.bookappstore.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shoppingcart")
public class ShoppingCartController {
    private ShoppingCartService shoppingCartService;

    @GetMapping
    public ShoppingCartResponseDto getShoppingCart(Authentication authentication) {
        String email = authentication.getName();
        return shoppingCartService.getShoppingCart(email);
    }
}
