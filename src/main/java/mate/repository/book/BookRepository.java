package mate.repository.book;

import java.util.List;
import mate.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @Query("FROM Book b WHERE UPPER(b.title) LIKE UPPER (:title)")
    List<Book> findAllByTitle(String title);
}
