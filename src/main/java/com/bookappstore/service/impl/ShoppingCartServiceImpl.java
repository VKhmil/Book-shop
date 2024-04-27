package com.bookappstore.service.impl;

import com.bookappstore.dto.cart.CartItemRequestDto;
import com.bookappstore.dto.cart.CartItemUpdateDto;
import com.bookappstore.dto.cart.ShoppingCartResponseDto;
import com.bookappstore.exception.EntityNotFoundException;
import com.bookappstore.mapper.CartItemMapper;
import com.bookappstore.mapper.ShoppingCartMapper;
import com.bookappstore.model.Book;
import com.bookappstore.model.CartItem;
import com.bookappstore.model.ShoppingCart;
import com.bookappstore.model.User;
import com.bookappstore.repository.book.BookRepository;
import com.bookappstore.repository.cart.CartItemRepository;
import com.bookappstore.repository.cart.ShoppingCartRepository;
import com.bookappstore.repository.user.UserRepository;
import com.bookappstore.service.ShoppingCartService;
import java.util.Optional;
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
    private final BookRepository bookRepository;

    @Override
    public ShoppingCartResponseDto getShoppingCart(Long userId) {
        ShoppingCart shoppingCartById = getShoppingCartByUserId(userId);
        return shoppingCartMapper.toDto(shoppingCartById);
    }

    @Override
    public ShoppingCartResponseDto addCartItem(
            Long userId,
            CartItemRequestDto cartItemRequestDto) {
        Book book = bookRepository.findById(cartItemRequestDto.getBookId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find book")
                );
        ShoppingCart shoppingCart = getShoppingCartByUserId(userId);
        Optional<CartItem> existingCartItem = shoppingCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getBook().getId().equals(book.getId()))
                .findFirst();

        existingCartItem.ifPresentOrElse(
                cartItem -> {
                    cartItem.setQuantity(cartItem.getQuantity() + cartItemRequestDto.getQuantity());
                    cartItemRepository.save(cartItem);
                },
                () -> {
                    CartItem newCartItem = cartItemMapper.toEntity(cartItemRequestDto);
                    newCartItem.setBook(book);
                    newCartItem.setShoppingCart(shoppingCart);
                    cartItemRepository.save(newCartItem);
                }
        );

        return shoppingCartMapper.toDto(getShoppingCartByUserId(userId));
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto updateCartItem(
            Long id,
            Long cartItemId,
            CartItemUpdateDto cartItemUpdateDto) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can't find cart item by item id: " + cartItemId)
        );
        CartItem cartItemDto = cartItemMapper.toEntity(cartItemUpdateDto);
        cartItem.setQuantity(cartItemDto.getQuantity());
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(getShoppingCartByUserId(id));
    }

    @Override
    public ShoppingCartResponseDto deleteCartItem(Long id, Long cartItemId) {
        ShoppingCart shoppingCart = getShoppingCartByUserId(id);
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(
                shoppingCart.getId(), cartItemId).orElseThrow(
                        () -> new EntityNotFoundException(
                        "Can't find cart item by item id: "
                                + cartItemId)
        );
        shoppingCart.removeItemFromCart(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    private ShoppingCart getShoppingCartByUserId(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find user by user id: " + id)
        );
        return shoppingCartRepository.findByUserId(user.getId()).orElse(
                shoppingCartRepository.save(new ShoppingCart())
        );
    }
}
