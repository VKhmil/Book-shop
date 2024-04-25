package com.bookappstore.service;

import com.bookappstore.dto.book.BookDto;
import com.bookappstore.dto.book.BookDtoWithoutCategoryIds;
import com.bookappstore.dto.book.BookSearchParametersDto;
import com.bookappstore.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto createBookRequestDto);

    List<BookDto> findAll(Pageable pageable); // pageable

    BookDto findById(Long id);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto params);

    BookDto update(Long id, CreateBookRequestDto createBookRequestDto);

    List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId);
}
