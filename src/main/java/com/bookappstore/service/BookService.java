package com.bookappstore.service;

import com.bookappstore.dto.BookDto;
import com.bookappstore.dto.BookSearchParametersDto;
import com.bookappstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto createBookRequestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto params);
}
