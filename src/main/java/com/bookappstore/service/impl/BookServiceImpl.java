package com.bookappstore.service.impl;

import com.bookappstore.dto.book.BookDto;
import com.bookappstore.dto.book.BookDtoWithoutCategoryIds;
import com.bookappstore.dto.book.BookSearchParametersDto;
import com.bookappstore.dto.book.CreateBookRequestDto;
import com.bookappstore.exception.EntityNotFoundException;
import com.bookappstore.mapper.BookMapper;
import com.bookappstore.model.Book;
import com.bookappstore.model.Category;
import com.bookappstore.repository.book.BookRepository;
import com.bookappstore.repository.book.spec.BookSpecificationBuilder;
import com.bookappstore.repository.category.CategoryRepository;
import com.bookappstore.service.BookService;
import java.util.List;
import java.util.Set;
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
    private final CategoryRepository categoryRepository;

    @Override
    public BookDto save(CreateBookRequestDto createBookRequestDto) {
        Set<Category> categories = categoryRepository
                .findByIdIn(createBookRequestDto.getCategoriesIds());
        Book book = bookMapper.toModel(createBookRequestDto);
        book.setCategories(categories);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAllWithCategories(pageable).stream()
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
        Set<Category> categories = categoryRepository
                .findByIdIn(requestDto.getCategoriesIds());
        Book bookById = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find by book id " + id)
        );
        bookMapper.updateFromDto(requestDto, bookById);
        Book updatedBook = bookRepository.save(bookById);
        return bookMapper.toDto(updatedBook);
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId) {
        return bookRepository.findAllByCategoryId(categoryId)
                .stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}

