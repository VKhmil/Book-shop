package mate.service;

import java.util.List;
import mate.dto.BookDto;
import mate.dto.BookSearchParametersDto;
import mate.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto createBookRequestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto params);
}
