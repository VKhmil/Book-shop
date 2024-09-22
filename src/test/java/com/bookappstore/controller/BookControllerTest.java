package com.bookappstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookappstore.dto.book.BookDto;
import com.bookappstore.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {

    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSourse,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSourse);
        try (Connection connection = dataSourse.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/test/books/add-books-to-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/test/categories/add-categories-to-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/test/books/add-books-and-categories-into-table.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    private static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/test/books/delete-books-categories.sql"
                    )
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/test/books/remove-books-from-table-books.sql"
                    )
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/test/categories/delete-categories.sql"
                    )
            );
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Update book")
    @Sql(scripts = {
            "classpath:database/test/books/add-one-book.sql",
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/test/books/delete-book-from-books_categories.sql",
            "classpath:database/test/books/remove-book-from-table-books.sql",
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateByValidId_ShouldReturnUpdateBook() throws Exception {
        long bookId = 3L;
        CreateBookRequestDto updateRequestDto = new CreateBookRequestDto()
                .setTitle("Updated Book Title")
                .setAuthor("Updated Author")
                .setDescription("Updated description for the book")
                .setIsbn("9783161484103")
                .setPrice(BigDecimal.valueOf(25.99))
                .setCoverImage("http://example.com/updated-cover.jpg")
                .setCategoriesIds(Set.of(13L));

        BookDto expectedDto = new BookDto()
                .setId(bookId)
                .setTitle(updateRequestDto.getTitle())
                .setAuthor(updateRequestDto.getAuthor())
                .setDescription(updateRequestDto.getDescription())
                .setIsbn(updateRequestDto.getIsbn())
                .setPrice(updateRequestDto.getPrice())
                .setCoverImage(updateRequestDto.getCoverImage())
                .setCategoriesIds(updateRequestDto.getCategoriesIds());

        String jsonRequest = objectMapper.writeValueAsString(updateRequestDto);

        MvcResult result = mockMvc.perform(
                        post("/books/3")
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
    @DisplayName("Save book")
    @Sql(scripts = {
            "classpath:database/test/categories/clear-categories.sql",
            "classpath:database/test/books/clear-books-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/test/books/delete-books-categories.sql",
            "classpath:database/test/categories/delete-categories.sql",
            "classpath:database/test/books/remove-books-from-table-books.sql",
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto()
                .setTitle("Test Book 1")
                .setAuthor("Test Author 1")
                .setDescription("Valid Description with less than 255 characters")
                .setIsbn("9783161484100")
                .setPrice(BigDecimal.valueOf(19.99))
                .setCoverImage("http://example.com/cover1.jpg")
                .setCategoriesIds(Set.of(12L, 13L));

        BookDto expectedDto = new BookDto()
                .setTitle(createBookRequestDto.getTitle())
                .setAuthor(createBookRequestDto.getAuthor())
                .setDescription(createBookRequestDto.getDescription())
                .setIsbn(createBookRequestDto.getIsbn())
                .setPrice(createBookRequestDto.getPrice())
                .setCoverImage(createBookRequestDto.getCoverImage())
                .setCategoriesIds(createBookRequestDto.getCategoriesIds());

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
    void findAllBook_GivenBooksInCatalog_ShouldReturnBooks() throws Exception {
        List<BookDto> expected = new ArrayList<>();
        expected.add(new BookDto()
                .setId(12L)
                .setTitle("Test Book 1")
                .setAuthor("Test Author 1")
                .setDescription("Description for Test Book 1")
                .setIsbn("9783161484100")
                .setPrice(BigDecimal.valueOf(19.99))
                .setCoverImage("http://example.com/cover1.jpg")
                .setCategoriesIds(Set.of(12L))
        );
        expected.add(new BookDto()
                .setId(13L)
                .setTitle("Test Book 2")
                .setAuthor("Test Author 2")
                .setDescription("Description for Test Book 2")
                .setIsbn("9783161484101")
                .setPrice(BigDecimal.valueOf(29.99))
                .setCoverImage("http://example.com/cover2.jpg")
                .setCategoriesIds(Set.of(12L))
        );

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
    @DisplayName("Find book by ID")
    @Sql(scripts = {
            "classpath:database/test/books/add-one-book.sql",
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/test/books/delete-book-from-books_categories.sql",
            "classpath:database/test/books/remove-book-from-table-books.sql",
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById_ByGivenId_ShouldReturnValidBook() throws Exception {
        long bookId = 3L;
        BookDto expected = new BookDto()
                .setId(bookId)
                .setTitle("Test Book 3")
                .setAuthor("Test Author 3")
                .setDescription("Description for Test Book 3")
                .setIsbn("9783161484107")
                .setPrice(BigDecimal.valueOf(40.99))
                .setCoverImage("http://example.com/cover3.jpg")
                .setCategoriesIds(Set.of());

        MvcResult result = mockMvc.perform(get("/books/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(),
                BookDto.class);
        Assertions.assertEquals(expected, actual);
    }
}
