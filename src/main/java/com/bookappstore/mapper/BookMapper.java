package com.bookappstore.mapper;

import com.bookappstore.config.MapperConfig;
import com.bookappstore.dto.book.BookDto;
import com.bookappstore.dto.book.CreateBookRequestDto;
import com.bookappstore.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    void updateFromDto(CreateBookRequestDto dto, @MappingTarget Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
