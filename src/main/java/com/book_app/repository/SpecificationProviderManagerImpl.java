package com.book_app.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import com.book_app.exception.SpecificationProviderException;
import com.book_app.model.Book;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SpecificationProviderManagerImpl implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(b -> b.getKey().equals(key))
                .findFirst()
                .orElseThrow(()
                        -> new SpecificationProviderException("Can't find correct "
                        + "specification provider for key " + key));
    }
}
