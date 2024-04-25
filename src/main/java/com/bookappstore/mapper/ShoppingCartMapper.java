package com.bookappstore.mapper;

import com.bookappstore.config.MapperConfig;
import com.bookappstore.dto.cart.CartItemResponseDto;
import com.bookappstore.dto.cart.ShoppingCartResponseDto;
import com.bookappstore.model.CartItem;
import com.bookappstore.model.ShoppingCart;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);

    ShoppingCart toEntity(ShoppingCartResponseDto shoppingCartResponseDto);

    @AfterMapping
    default void setShoppingCartResponseDtoParams(
            @MappingTarget ShoppingCartResponseDto shoppingCartResponseDto,
            ShoppingCart shoppingCart
    ) {
        shoppingCartResponseDto.setUserId(shoppingCart.getUser().getId());
        Set<CartItemResponseDto> cartItemSet = new HashSet<>();
        for (CartItem item : shoppingCart.getCartItems()) {
            CartItemResponseDto cartItem = new CartItemResponseDto();
            cartItem.setId(item.getId());
            cartItem.setBookId(item.getBook().getId());
            cartItem.setBookTitle(item.getBook().getTitle());
            cartItem.setQuantity(item.getQuantity());
            cartItemSet.add(cartItem);
        }
        shoppingCartResponseDto.setCartItemResponseDto(cartItemSet);
    }
}
