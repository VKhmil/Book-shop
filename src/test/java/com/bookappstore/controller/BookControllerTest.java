package com.bookappstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookappstore.dto.book.BookDto;
import com.bookappstore.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {
        "classpath:database/test/books/add-books-to-table.sql",
        "classpath:database/test/categories/add-categories-to-table.sql",
        "classpath:database/test/books/add-books-and-categories-into-table.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:database/test/books/delete-books-categories.sql",
        "classpath:database/test/books/remove-books-from-table-books.sql",
        "classpath:database/test/categories/delete-categories.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BookControllerTest {

    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Given valid book ID and update request, should return updated book")
    void updateByValidId_ShouldReturnUpdatedBook_Ok() throws Exception {
        long bookId = 12L;
        CreateBookRequestDto updateRequestDto = createUpdateRequestDto();
        BookDto expectedDto = createExpectedBookDto(bookId, updateRequestDto);

        String jsonRequest = objectMapper.writeValueAsString(updateRequestDto);

        MvcResult result = mockMvc.perform(
                        post("/books/" + bookId)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actualDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(),
                BookDto.class);
        Assertions.assertEquals(expectedDto, actualDto);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Given valid create request, should create a new book")
    void createBook_ValidRequest_ShouldCreateBook_Ok() throws Exception {
        CreateBookRequestDto createBookRequestDto = createBookRequestDto();
        BookDto expectedDto = createExpectedBookDtoWithoutId(createBookRequestDto);

        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);
        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actualDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(),
                BookDto.class);
        EqualsBuilder.reflectionEquals(expectedDto, actualDto, "id");
        Assertions.assertNotNull(actualDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Find all books in the catalog, should return all books")
    void findAllBooks_BooksExistInCatalog_ShouldReturnAllBooks_Ok() throws Exception {
        List<BookDto> expected = createBooksList();

        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(),
                BookDto[].class);

        Assertions.assertEquals(2, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Find book by ID, should return the valid book")
    void findById_ValidId_ShouldReturnBook_Ok() throws Exception {
        long bookId = 12L;
        BookDto expected = createBookDto(bookId);

        MvcResult result = mockMvc.perform(get("/books/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(),
                BookDto.class);
        Assertions.assertEquals(expected, actual);
    }

    private CreateBookRequestDto createUpdateRequestDto() {
        return new CreateBookRequestDto()
                .setTitle("Updated Book Title")
                .setAuthor("Updated Author")
                .setDescription("Updated description for the book")
                .setIsbn("9783161484103")
                .setPrice(BigDecimal.valueOf(25.99))
                .setCoverImage("http://example.com/updated-cover.jpg")
                .setCategoriesIds(Set.of(13L));
    }

    private CreateBookRequestDto createBookRequestDto() {
        return new CreateBookRequestDto()
                .setTitle("Test Book 10")
                .setAuthor("Test Author 10")
                .setDescription("Valid Description with less than 255 characters")
                .setIsbn("9783161484110")
                .setPrice(BigDecimal.valueOf(39.99))
                .setCoverImage("http://example.com/cover10.jpg")
                .setCategoriesIds(Set.of(12L, 13L));
    }

    private BookDto createExpectedBookDto(long bookId, CreateBookRequestDto requestDto) {
        return new BookDto()
                .setId(bookId)
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setDescription(requestDto.getDescription())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setCoverImage(requestDto.getCoverImage())
                .setCategoriesIds(requestDto.getCategoriesIds());
    }

    private BookDto createExpectedBookDtoWithoutId(CreateBookRequestDto requestDto) {
        return new BookDto()
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setDescription(requestDto.getDescription())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setCoverImage(requestDto.getCoverImage())
                .setCategoriesIds(requestDto.getCategoriesIds());
    }

    private List<BookDto> createBooksList() {
        List<BookDto> books = new ArrayList<>();
        books.add(new BookDto()
                .setId(12L)
                .setTitle("Test Book 1")
                .setAuthor("Test Author 1")
                .setDescription("Description for Test Book 1")
                .setIsbn("9783161484100")
                .setPrice(BigDecimal.valueOf(19.99))
                .setCoverImage("http://example.com/cover1.jpg")
                .setCategoriesIds(Set.of(12L)));
        books.add(new BookDto()
                .setId(13L)
                .setTitle("Test Book 2")
                .setAuthor("Test Author 2")
                .setDescription("Description for Test Book 2")
                .setIsbn("9783161484101")
                .setPrice(BigDecimal.valueOf(29.99))
                .setCoverImage("http://example.com/cover2.jpg")
                .setCategoriesIds(Set.of(12L)));
        return books;
    }

    private BookDto createBookDto(long id) {
        return new BookDto()
                .setId(id)
                .setTitle("Test Book 1")
                .setAuthor("Test Author 1")
                .setDescription("Description for Test Book 1")
                .setIsbn("9783161484100")
                .setPrice(BigDecimal.valueOf(19.99))
                .setCoverImage("http://example.com/cover1.jpg")
                .setCategoriesIds(Set.of(12L));
    }
}
