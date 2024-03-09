package mate.repository;

import java.util.List;
import java.util.Optional;
import mate.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findById(Long id);

    @Query("FROM Book b WHERE UPPER(b.title) LIKE UPPER (:title)")
    List<Book> findAllByTitle(String title);
}
