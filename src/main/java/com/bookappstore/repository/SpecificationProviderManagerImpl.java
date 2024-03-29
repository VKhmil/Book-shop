package com.bookappstore.repository;

import com.bookappstore.exception.SpecificationProviderException;
import com.bookappstore.model.Book;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
