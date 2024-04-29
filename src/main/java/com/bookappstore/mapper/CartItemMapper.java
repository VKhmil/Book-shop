package com.bookappstore.mapper;

import com.bookappstore.config.MapperConfig;
import com.bookappstore.dto.cart.CartItemRequestDto;
import com.bookappstore.dto.cart.CartItemResponseDto;
import com.bookappstore.dto.cart.ShoppingCartDto;
import com.bookappstore.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(target = "book", ignore = true)
    CartItem toEntity(CartItemRequestDto cartItemRequestDto);

    CartItemResponseDto toDto(ShoppingCartDto cartItem);
}
