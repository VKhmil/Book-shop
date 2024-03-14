package com.book_app.mapper;

import com.book_app.config.MapperConfig;
import com.book_app.dto.BookDto;
import com.book_app.dto.CreateBookRequestDto;
import com.book_app.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

}
