package com.bookappstore.mapper;

import com.bookappstore.config.MapperConfig;
import com.bookappstore.dto.cart.CartItemRequestDto;
import com.bookappstore.dto.cart.CartItemUpdateDto;
import com.bookappstore.model.Book;
import com.bookappstore.model.CartItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(target = "book", ignore = true)
    CartItem toEntity(CartItemRequestDto dto);

    @Mapping(target = "book", ignore = true)
    CartItem toDto(CartItemUpdateDto entity);

    @AfterMapping
    default void setBookId(@MappingTarget CartItem cartItem,
                           CartItemRequestDto cartItemRequestDto) {
        Book book = new Book();
        book.setId(cartItemRequestDto.getBookId());
        cartItem.setBook(book);
    }
}
