package com.bookappstore.mapper;

import com.bookappstore.config.MapperConfig;
import com.bookappstore.dto.cart.ShoppingCartDto;
import com.bookappstore.model.Order;
import com.bookappstore.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class,
        uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "cartItems", target = "cartItems")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "cartItems", target = "cartItems")
    ShoppingCart toEntity(ShoppingCartDto shoppingCartDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderItems", source = "cartItems")
    Order toOrder(ShoppingCart shoppingCart);
}
