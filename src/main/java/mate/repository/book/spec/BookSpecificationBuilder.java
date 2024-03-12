package mate.repository.book.spec;

import lombok.RequiredArgsConstructor;
import mate.dto.BookSearchParametersDto;
import mate.model.Book;
import mate.repository.SpecificationBuilder;
import mate.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {

    private SpecificationProviderManager<Book> bookSpecificationProviderManager;

    public enum SearchCriteria {
        AUTHOR, TITLE, ISBN, PRICE, DESCRIPTION
    }

    @Override
    public Specification<Book> build(BookSearchParametersDto bookSearchParametersDto) {
        Specification<Book> specification = Specification.where(null);

        if (bookSearchParametersDto.authors() != null && bookSearchParametersDto
                .authors().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(SearchCriteria.AUTHOR.name())
                    .getSpecification(bookSearchParametersDto.authors()));
        }

        if (bookSearchParametersDto.titles() != null && bookSearchParametersDto
                .titles().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(SearchCriteria.TITLE.name())
                    .getSpecification(bookSearchParametersDto.titles()));
        }

        if (bookSearchParametersDto.isbn() != null && bookSearchParametersDto.isbn().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(SearchCriteria.ISBN.name())
                    .getSpecification(bookSearchParametersDto.isbn()));
        }

        if (bookSearchParametersDto.price() != null && bookSearchParametersDto.price().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(SearchCriteria.PRICE.name())
                    .getSpecification(bookSearchParametersDto.price()));
        }

        if (bookSearchParametersDto.description() != null && bookSearchParametersDto
                .description().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(SearchCriteria.DESCRIPTION.name())
                    .getSpecification(bookSearchParametersDto.description()));
        }

        return null;
    }
}

