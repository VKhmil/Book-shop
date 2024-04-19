package com.bookappstore.service.impl;

import com.bookappstore.dto.book.BookDto;
import com.bookappstore.dto.book.BookSearchParametersDto;
import com.bookappstore.dto.book.CreateBookRequestDto;
import com.bookappstore.exception.EntityNotFoundException;
import com.bookappstore.mapper.BookMapper;
import com.bookappstore.model.Book;
import com.bookappstore.repository.book.BookRepository;
import com.bookappstore.repository.book.spec.BookSpecificationBuilder;
import com.bookappstore.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id" + id)
        );
        return bookMapper.toDto(book);
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

    @Override
    public BookDto update(Long id, CreateBookRequestDto requestDto) {
        Book bookById = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find by book id " + id)
        );
        bookMapper.updateFromDto(requestDto, bookById);
        Book updatedBook = bookRepository.save(bookById);
        return bookMapper.toDto(updatedBook);
    }
}

