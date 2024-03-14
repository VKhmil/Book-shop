package com.book_app.repository.book.spec;

import jakarta.persistence.criteria.Predicate;
import com.book_app.model.Book;
import com.book_app.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class DescriptionSpecificationProvider implements SpecificationProvider<Book> {
    private static final String KEY_WORD = "description";

    @Override
    public String getKey() {
        return KEY_WORD;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder
                    .like(root.get(KEY_WORD),
                            "%" + params[0] + "%");
            return criteriaBuilder.and(predicate);
        };
    }
}
