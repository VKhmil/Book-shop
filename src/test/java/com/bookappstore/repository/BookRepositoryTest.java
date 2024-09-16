package com.bookappstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.bookappstore.model.Book;
import com.bookappstore.model.Category;
import com.bookappstore.repository.book.BookRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace =
        AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find books by existed category id")
    @Sql(scripts = {
            "classpath:database/test/books/add-books-to-table.sql",
            "classpath:database/test/categories/add-categories-to-table.sql",
            "classpath:database/test/books/add-books-and-categories-into-table.sql",
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/test/books/delete-books-categories.sql",
            "classpath:database/test/books/remove-books-from-table-books.sql",
            "classpath:database/test/categories/delete-categories.sql",
            },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_ExistedId_Success() {
        Category category = createCategory(1L, "Fiction");

        Book firstBook = new Book();
        firstBook.setId(1L);
        firstBook.setTitle("Test Book 1");
        firstBook.setAuthor("Test Author 1");
        firstBook.setIsbn("978-3-16-148410-0");
        firstBook.setPrice(BigDecimal.valueOf(19.99));
        firstBook.setDescription("Description for Test Book 1");
        firstBook.setCoverImage("http://example.com/cover1.jpg");
        firstBook.setCategories(Set.of(category));

        Book secondBook = new Book();
        secondBook.setId(2L);
        secondBook.setTitle("Test Book 2");
        secondBook.setAuthor("Test Author 2");
        secondBook.setIsbn("978-3-16-148410-1");
        secondBook.setPrice(BigDecimal.valueOf(29.99));
        secondBook.setDescription("Description for Test Book 2");
        secondBook.setCoverImage("http://example.com/cover2.jpg");
        secondBook.setCategories(Set.of(category));

        List<Book> expected = List.of(firstBook, secondBook);

        List<Book> actual = bookRepository.findAllByCategoryId(1L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    private Category createCategory(Long id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setDeleted(false);
        return category;
    }
}
