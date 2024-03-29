package com.bookappstore.repository.book.spec;

import com.bookappstore.dto.BookSearchParametersDto;
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
        DESCRIPTION("description");

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

        if (bookSearchParametersDto.authors() != null && bookSearchParametersDto
                .authors().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(SearchCriteria.AUTHOR.getValue())
                    .getSpecification(bookSearchParametersDto.authors()));
        }

        if (bookSearchParametersDto.titles() != null && bookSearchParametersDto
                .titles().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(SearchCriteria.TITLE.getValue())
                    .getSpecification(bookSearchParametersDto.titles()));
        }

        if (bookSearchParametersDto.isbn() != null && bookSearchParametersDto.isbn().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(SearchCriteria.ISBN.getValue())
                    .getSpecification(bookSearchParametersDto.isbn()));
        }

        if (bookSearchParametersDto.price() != null && bookSearchParametersDto.price().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(SearchCriteria.PRICE.getValue())
                    .getSpecification(bookSearchParametersDto.price()));
        }

        if (bookSearchParametersDto.description() != null && bookSearchParametersDto
                .description().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(SearchCriteria.DESCRIPTION.getValue())
                    .getSpecification(bookSearchParametersDto.description()));
        }

        return specification;
    }
}
