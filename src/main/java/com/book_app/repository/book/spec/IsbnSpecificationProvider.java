package com.book_app.repository.book.spec;

import java.util.Arrays;
import com.book_app.model.Book;
import com.book_app.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {
    private static final String KEY_WORD = "isbn";

    @Override
    public String getKey() {
        return KEY_WORD;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root.get(KEY_WORD)
                .in(Arrays.stream(params).toArray());
    }
}
