package com.bookappstore.service.impl;

import com.bookappstore.dto.cart.ShoppingCartResponseDto;
import com.bookappstore.exception.EntityNotFoundException;
import com.bookappstore.mapper.CartItemMapper;
import com.bookappstore.mapper.ShoppingCartMapper;
import com.bookappstore.model.ShoppingCart;
import com.bookappstore.model.User;
import com.bookappstore.repository.cart.CartItemRepository;
import com.bookappstore.repository.cart.ShoppingCartRepository;
import com.bookappstore.repository.user.UserRepository;
import com.bookappstore.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final UserRepository userRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartResponseDto getShoppingCart(String email) {
        ShoppingCart shoppingCart = getShoppingCartByEmail(email);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    private ShoppingCart getShoppingCartByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("Can't find user by user email: " + email)
        );
        return shoppingCartRepository.findByUserId(user.getId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find shopping cart by id " + user.getId())
        );
    }
}
