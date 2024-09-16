package com.bookappstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.bookappstore.dto.book.BookDto;
import com.bookappstore.dto.book.BookSearchParametersDto;
import com.bookappstore.dto.book.CreateBookRequestDto;
import com.bookappstore.exception.EntityNotFoundException;
import com.bookappstore.mapper.BookMapper;
import com.bookappstore.model.Book;
import com.bookappstore.model.Category;
import com.bookappstore.repository.book.BookRepository;
import com.bookappstore.repository.book.spec.BookSpecificationBuilder;
import com.bookappstore.repository.category.CategoryRepository;
import com.bookappstore.service.impl.BookServiceImpl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceImpTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Verify the correct fields were returned when book exists")
    void findBookById_WithValidBookId_ShouldReturnUser() {
        Long idBook = 1L;
        Book book = createTestBookWithoutId();
        BookDto bookDto = createBookDto();

        when(bookRepository.findBookById(idBook)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto bookDtoServiceById = bookService.findById(idBook);

        assertThat(bookDtoServiceById).isEqualTo(bookDto);
        verify(bookRepository, times(1)).findBookById(idBook);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Using wrong id, method must throw an exception")
    void getById_NonExistingId_ShouldReturnException() {
        when(bookRepository.findBookById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(anyLong()));
    }

    @Test
    @DisplayName("Saving book with correct fields in a right way")
    void save_NormalBook_ShouldReturnBook() {
        CreateBookRequestDto testBookRequestDto = createTestBookRequestDto();
        Book bookWithoutId = createTestBookWithoutId();
        BookDto bookDto = createBookDto();

        when(bookMapper.toModel(testBookRequestDto)).thenReturn(bookWithoutId);
        when(bookRepository.save(bookWithoutId)).thenReturn(bookWithoutId);
        when(bookMapper.toDto(bookWithoutId)).thenReturn(bookDto);

        BookDto bookServiceById = bookService.save(testBookRequestDto);

        assertThat(bookServiceById).isEqualTo(bookDto);
    }

    @Test
    public void testFindAllBooksWithCategories() {

        Book book1 = createTestBook();
        book1.setId(1L);

        Book book2 = createTestBook();
        book2.setId(2L);

        BookDto bookDto1 = createBookDto();
        bookDto1.setTitle("Book 1");
        bookDto1.setAuthor("Author 1");

        BookDto bookDto2 = createBookDto();
        bookDto2.setTitle("Book 2");
        bookDto2.setAuthor("Author 2");

        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book1, book2);
        List<BookDto> bookDtos = List.of(bookDto1, bookDto2);

        Mockito.when(bookRepository.findAllWithCategories(pageable)).thenReturn(books);
        Mockito.when(bookMapper.toDtoList(books)).thenReturn(bookDtos);

        List<BookDto> result = bookService.findAll(pageable);

        assertNotNull(result);
        assertEquals(bookDtos.size(), result.size());
        verify(bookRepository).findAllWithCategories(pageable);
        verify(bookMapper).toDtoList(books);
    }

    @Test
    @DisplayName("Update book by Id")
    public void updateBook_UpdateBookById_Correct() {
        Set<Category> categories = Set.of(createCategory(1L, "Fiction"));
        Book book = createTestBook();
        CreateBookRequestDto createBookRequestDto = createTestBookRequestDto();
        Mockito.when(categoryRepository.findByIdIn(createBookRequestDto.getCategoriesIds()))
                .thenReturn(categories);

        book.setCategories(categories);
        book.setTitle(createBookRequestDto.getTitle());
        book.setAuthor(createBookRequestDto.getAuthor());
        book.setDescription(createBookRequestDto.getDescription());
        book.setPrice(createBookRequestDto.getPrice());
        book.setIsbn(createBookRequestDto.getIsbn());
        book.setCoverImage(createBookRequestDto.getCoverImage());
        Long idTest = 1L;

        Mockito.when(bookRepository.findById(idTest)).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDto expectedBookDto = createBookDto();
        expectedBookDto.setTitle(createBookRequestDto.getTitle());
        expectedBookDto.setAuthor(createBookRequestDto.getAuthor());
        expectedBookDto.setDescription(createBookRequestDto.getDescription());
        expectedBookDto.setPrice(createBookRequestDto.getPrice());
        expectedBookDto.setIsbn(createBookRequestDto.getIsbn());
        expectedBookDto.setCoverImage(createBookRequestDto.getCoverImage());

        Mockito.when(bookMapper.toDto(any(Book.class))).thenReturn(expectedBookDto);

        BookDto result = bookService.update(idTest, createBookRequestDto);

        assertNotNull(result);
        assertEquals(expectedBookDto, result);

        verify(bookRepository).findById(idTest);
        verify(categoryRepository).findByIdIn(createBookRequestDto.getCategoriesIds());
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Search books by parameters")
    public void testSearchBooksByParameters() {
        BookSearchParametersDto searchParams = createTestSearchParameters();

        Specification<Book> specification = Mockito.mock(Specification.class);

        List<Book> books = new ArrayList<>();
        books.add(createBookWithParams(1L, "Test Title 1",
                "Test Author 1",
                BigDecimal.valueOf(19.99)));
        books.add(createBookWithParams(2L,
                "Test Title 2",
                "Test Author 2",
                BigDecimal.valueOf(29.99)));

        List<BookDto> bookDtos = List.of(
                createBookDtoWithParams("Test Title 1", "Test Author 1", BigDecimal.valueOf(19.99)),
                createBookDtoWithParams("Test Title 2", "Test Author 2", BigDecimal.valueOf(29.99))
        );

        Mockito.when(bookSpecificationBuilder.build(searchParams)).thenReturn(specification);
        Mockito.when(bookRepository.findAll(specification)).thenReturn(books);
        Mockito.when(bookMapper.toDtoList(books)).thenReturn(bookDtos);

        List<BookDto> result = bookService.search(searchParams);

        assertNotNull(result);
        assertEquals(bookDtos.size(), result.size());

        verify(bookSpecificationBuilder).build(searchParams);
        verify(bookRepository).findAll(specification);
        verify(bookMapper).toDtoList(books);
    }

    private Category createCategory(Long id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setDeleted(false);
        return category;
    }

    private Book createTestBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test title");
        book.setAuthor("Test author");
        book.setPrice(BigDecimal.valueOf(50));
        book.setDescription("Test description");
        book.setIsbn("978-3-16-148410-0");
        book.setCoverImage("http://example.com/cover.jpg");
        return book;
    }

    private Book createTestBookWithoutId() {
        return createTestBook();
    }

    private BookDto createBookDto() {
        return new BookDto()
                .setId(1L)
                .setTitle("Test title")
                .setAuthor("Test author")
                .setPrice(BigDecimal.valueOf(50))
                .setDescription("Test description")
                .setIsbn("978-3-16-148410-0")
                .setCoverImage("http://example.com/cover.jpg");
    }

    private CreateBookRequestDto createTestBookRequestDto() {
        return new CreateBookRequestDto()
                .setTitle("Test Book Title")
                .setAuthor("Test Author")
                .setIsbn("978-3-16-148410-0")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("A detailed description of the book.")
                .setCoverImage("http://example.com/cover.jpg")
                .setCategoriesIds(Set.of(1L));
    }

    private BookSearchParametersDto createTestSearchParameters() {
        return new BookSearchParametersDto(
                new String[]{"Test Book Title 1", "Test Book Title 2"},
                new String[]{"Test Author 1", "Test Author 2"},
                new String[]{"978-3-16-148410-0", "978-1-23-456789-7"},
                new String[]{"19.99", "29.99"},
                new String[]{"Description of book 1", "Description of book 2"},
                new String[]{"Fiction", "Science"}
        );
    }

    private Book createBookWithParams(Long id, String title, String author, BigDecimal price) {
        Book book = createTestBook();
        book.setId(id);
        book.setTitle(title);
        book.setAuthor(author);
        book.setPrice(price);
        return book;
    }

    private BookDto createBookDtoWithParams(String title, String author, BigDecimal price) {
        BookDto bookDto = createBookDto();
        bookDto.setTitle(title);
        bookDto.setAuthor(author);
        bookDto.setPrice(price);
        return bookDto;
    }
}
