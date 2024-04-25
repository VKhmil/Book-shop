package com.bookappstore.repository.book.spec;

import com.bookappstore.dto.book.BookSearchParametersDto;
import com.bookappstore.model.Book;
import com.bookappstore.repository.SpecificationBuilder;
import com.bookappstore.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {

    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    public enum SearchCriteria {
        AUTHOR("author"),
        TITLE("title"),
        ISBN("isbn"),
        PRICE("price"),
        DESCRIPTION("description"),
        CATEGORY("category");

        private final String value;

        SearchCriteria(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @Override
    public Specification<Book> build(BookSearchParametersDto bookSearchParametersDto) {
        Specification<Book> specification = Specification.where(null);

        addSpecification(specification, bookSearchParametersDto.authors(),
                SearchCriteria.AUTHOR);
        addSpecification(specification, bookSearchParametersDto.titles(),
                SearchCriteria.TITLE);
        addSpecification(specification, bookSearchParametersDto.isbn(),
                SearchCriteria.ISBN);
        addSpecification(specification, bookSearchParametersDto.price(),
                SearchCriteria.PRICE);
        addSpecification(specification, bookSearchParametersDto.description(),
                SearchCriteria.DESCRIPTION);
        addSpecification(specification, bookSearchParametersDto.categories(),
                SearchCriteria.CATEGORY);

        return specification;
    }

    private void addSpecification(Specification<Book> specification,
                                  String[] values,
                                  SearchCriteria searchCriteria) {
        if (values != null && values.length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(searchCriteria.getValue())
                    .getSpecification(values));
        }
    }
}
