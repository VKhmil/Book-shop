package com.book_app.service;

import java.util.List;
import com.book_app.dto.BookDto;
import com.book_app.dto.BookSearchParametersDto;
import com.book_app.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto createBookRequestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto params);
}
