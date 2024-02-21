package mate.service;

import java.util.List;
import mate.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
