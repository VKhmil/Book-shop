package mate.repository;

import java.util.List;
import mate.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
