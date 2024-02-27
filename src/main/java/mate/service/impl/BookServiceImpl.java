package mate.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.dto.BookDto;
import mate.dto.CreateBookRequestDto;
import mate.exception.EntityNotFoundException;
import mate.mapper.BookMapper;
import mate.model.Book;
import mate.repository.BookRepository;
import mate.service.BookService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto createBookRequestDto) {
        Book book = bookMapper.toModel(createBookRequestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find employee by id" + id)
        );
        return bookMapper.toDto(book);
    }
}
