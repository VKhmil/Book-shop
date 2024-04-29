package com.bookappstore.service.impl;

import com.bookappstore.dto.cart.CartItemRequestDto;
import com.bookappstore.dto.cart.CartItemResponseDto;
import com.bookappstore.dto.cart.ShoppingCartDto;
import com.bookappstore.exception.EntityNotFoundException;
import com.bookappstore.mapper.CartItemMapper;
import com.bookappstore.model.CartItem;
import com.bookappstore.model.ShoppingCart;
import com.bookappstore.repository.book.BookRepository;
import com.bookappstore.repository.cart.CartItemRepository;
import com.bookappstore.repository.cart.ShoppingCartRepository;
import com.bookappstore.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public CartItemResponseDto saveItemToShoppingCart(CartItemRequestDto requestDto,
                                                      ShoppingCartDto shoppingCartDto) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .findById(shoppingCartDto.getUserId())
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Shopping cart not found"));

        CartItem cartItem = cartItemMapper.toEntity(requestDto);
        cartItem.setBook(bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Can not find book by id: "
                        + requestDto.getBookId())));

        shoppingCart.getCartItems().add(cartItem);
        shoppingCartRepository.save(shoppingCart);
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    public CartItemResponseDto getById(Long id) {
        return cartItemMapper.toDto(cartItemRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can not find cart item by id:" + id)));
    }

    @Override
    public CartItemResponseDto delete(Long id) {
        cartItemRepository.deleteById(id);
        return null;
    }

    @Override
    public void addQuantity(Long userId, int quantity) {
        shoppingCartRepository.findByUserId(userId).ifPresent(shoppingCart -> {
            shoppingCart.getCartItems().forEach(cartItem ->
                    cartItem.setQuantity(cartItem.getQuantity() + quantity));
            shoppingCartRepository.save(shoppingCart);
        });
    }
}
