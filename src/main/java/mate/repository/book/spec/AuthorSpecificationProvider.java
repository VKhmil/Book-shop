package mate.repository.book.spec;

import java.util.Arrays;
import mate.model.Book;
import mate.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    private static final String KEY_WORD = "author";

    @Override
    public String getKey() {
        return KEY_WORD;
    }

    public Specification<Book> getSpecification(String[] param) {
        return (root, query, criteriaBuilder)
                -> root.get(KEY_WORD).in(Arrays.stream(param).toArray());
    }
}
