package com.bookappstore.service.impl;

import com.bookappstore.dto.cart.CartItemRequestDto;
import com.bookappstore.dto.cart.CartItemUpdateDto;
import com.bookappstore.dto.cart.ShoppingCartResponseDto;
import com.bookappstore.exception.EntityNotFoundException;
import com.bookappstore.mapper.CartItemMapper;
import com.bookappstore.mapper.ShoppingCartMapper;
import com.bookappstore.model.CartItem;
import com.bookappstore.model.ShoppingCart;
import com.bookappstore.model.User;
import com.bookappstore.repository.cart.CartItemRepository;
import com.bookappstore.repository.cart.ShoppingCartRepository;
import com.bookappstore.repository.user.UserRepository;
import com.bookappstore.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final UserRepository userRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartResponseDto getShoppingCartDtoByUserId(Long id) {
        ShoppingCart shoppingCartById = getShoppingCartById(id);
        return shoppingCartMapper.toDto(shoppingCartById);
    }

    @Override
    public ShoppingCartResponseDto addCartItemByUserId(
            Long id,
            CartItemRequestDto cartItemRequestDto) {
        CartItem cartItem = cartItemMapper.toEntity(cartItemRequestDto);
        ShoppingCart shoppingCart = getShoppingCartById(id);
        cartItem.setShoppingCart(shoppingCart);
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(getShoppingCartById(id));
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto updateCartItem(
            Long id,
            Long cartItemId,
            CartItemUpdateDto cartItemUpdateDto) {
        CartItem cartItemDb = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can't find cart item by item id: " + cartItemId)
        );
        CartItem cartItemDto = cartItemMapper.toEntity(cartItemUpdateDto);
        cartItemDb.setQuantity(cartItemDto.getQuantity());
        cartItemRepository.save(cartItemDb);
        return shoppingCartMapper.toDto(getShoppingCartById(id));
    }

    @Override
    public ShoppingCartResponseDto deleteCartItem(Long id, Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
        return shoppingCartMapper.toDto(getShoppingCartById(id));
    }

    public ShoppingCart getShoppingCartById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find user by user id: " + id)
        );
        return shoppingCartRepository.findByUserId(user.getId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find shopping cart by id " + user.getId())
        );
    }
}
