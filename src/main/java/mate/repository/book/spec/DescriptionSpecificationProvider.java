package mate.repository.book.spec;

import jakarta.persistence.criteria.Predicate;
import mate.model.Book;
import mate.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class DescriptionSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "description";
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder
                    .like(root.get("description"),
                            "%" + params[0] + "%");
            return criteriaBuilder.and(predicate);
        };
    }
}
