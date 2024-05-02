package com.bookappstore.service.impl;

import com.bookappstore.dto.cart.CartItemRequestDto;
import com.bookappstore.dto.cart.CartItemUpdateDto;
import com.bookappstore.dto.cart.ShoppingCartDto;
import com.bookappstore.exception.EntityNotFoundException;
import com.bookappstore.mapper.ShoppingCartMapper;
import com.bookappstore.model.Book;
import com.bookappstore.model.CartItem;
import com.bookappstore.model.ShoppingCart;
import com.bookappstore.repository.book.BookRepository;
import com.bookappstore.repository.cart.CartItemRepository;
import com.bookappstore.repository.cart.ShoppingCartRepository;
import com.bookappstore.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public ShoppingCartDto getShoppingCart(Long userId) {
        return shoppingCartMapper
                .toDto(getShoppingCartByUserId(userId));
    }

    @Override
    @Transactional
    public ShoppingCartDto addCartItemToShoppingCart(Long userId,
                                                     CartItemRequestDto itemDto) {
        Book book = bookRepository.findById(itemDto.getBookId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find book"));

        ShoppingCart cart = getShoppingCartByUserId(userId);
        cart.getCartItems().stream().filter(cartItem -> cartItem.getBook().getId()
                        .equals(itemDto.getBookId()))
                .findFirst()
                .ifPresentOrElse(cartItem -> cartItem.setQuantity(cartItem.getQuantity()
                                + itemDto.getQuantity()),
                        () -> addCartItemToShoppingCart(itemDto, book, cart));

        shoppingCartRepository.save(cart);
        return shoppingCartMapper.toDto(cart);
    }

    private void addCartItemToShoppingCart(CartItemRequestDto itemDto,
                                           Book book, ShoppingCart cart) {
        CartItem newCartItem = new CartItem();
        newCartItem.setBook(book);
        newCartItem.setQuantity(itemDto.getQuantity());
        newCartItem.setShoppingCart(cart);
        cart.getCartItems().add(newCartItem);
    }

    @Override
    @Transactional
    public ShoppingCartDto updateCartItem(Long userId,
                                          Long cartItemId,
                                          CartItemUpdateDto request) {
        cartItemRepository.updateCartItemQuantityById(
                request.getQuantity(),
                cartItemId);
        return shoppingCartMapper.toDto(getShoppingCartByUserId(userId));
    }

    @Override
    @Transactional
    public ShoppingCartDto removeCartItem(Long userId, Long cartItemId) {
        ShoppingCart shoppingCart = getShoppingCartByUserId(userId);
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(
                cartItemId, shoppingCart.getId());
        shoppingCart.removeItemFromCart(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    private ShoppingCart getShoppingCartByUserId(Long userId) {
        return shoppingCartRepository
                .findByUserId(userId).orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can't find user by id" + userId
                        )
                );
    }
}
