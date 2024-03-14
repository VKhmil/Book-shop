package com.bookappstore.service;

import java.util.List;
import com.bookappstore.dto.BookDto;
import com.bookappstore.dto.BookSearchParametersDto;
import com.bookappstore.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto createBookRequestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto params);
}
