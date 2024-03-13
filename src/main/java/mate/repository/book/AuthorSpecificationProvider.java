package mate.repository.book;

import java.util.Arrays;
import mate.model.Book;
import mate.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "Author";
    }

    public Specification<Book> getSpecification(String[] param) {
        return (root, query, criteriaBuilder)
                -> root.get("author").in(Arrays.stream(param).toArray());
    }
}
