package com.bookappstore.repository.book.spec;

import java.util.Arrays;
import com.bookappstore.model.Book;
import com.bookappstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    private static final String KEY_WORD = "title";

    @Override
    public String getKey() {
        return KEY_WORD;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder)
                -> root.get(KEY_WORD).in(Arrays.stream(params).toArray());
    }
}
