package mate.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.dto.BookDto;
import mate.dto.BookSearchParametersDto;
import mate.dto.CreateBookRequestDto;
import mate.exception.EntityNotFoundException;
import mate.mapper.BookMapper;
import mate.model.Book;
import mate.repository.book.BookRepository;
import mate.repository.book.spec.BookSpecificationBuilder;
import mate.service.BookService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

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

    @Override
    public List<BookDto> getAllByName(String title) {
        return bookRepository.findAllByTitle(title).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto params) {
        Specification<Book> build = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(build)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
