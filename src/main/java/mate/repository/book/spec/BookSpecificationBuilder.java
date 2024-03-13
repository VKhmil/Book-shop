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

    @Override
    public Specification<Book> build(BookSearchParametersDto bookSearchParametersDto) {
        Specification<Book> specification = Specification.where(null);
        if (bookSearchParametersDto.authors() != null && bookSearchParametersDto
                .authors().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider("author")
                    .getSpecification(bookSearchParametersDto.authors()));
        }

        if (bookSearchParametersDto.titles() != null && bookSearchParametersDto
                .authors().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider("title")
                    .getSpecification(bookSearchParametersDto.titles()));
        }
        return null;
    }
}
