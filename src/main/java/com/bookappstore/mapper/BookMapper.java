package com.bookappstore.mapper;

import com.bookappstore.config.MapperConfig;
import com.bookappstore.dto.book.BookDto;
import com.bookappstore.dto.book.BookDtoWithoutCategoryIds;
import com.bookappstore.dto.book.CreateBookRequestDto;
import com.bookappstore.model.Book;
import com.bookappstore.model.Category;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    @Mapping(target = "categoriesIds", ignore = true)
    BookDto toDto(Book book);

    void updateFromDto(CreateBookRequestDto dto, @MappingTarget Book book);

    Book toModel(CreateBookRequestDto requestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    List<BookDto> toDtoList(List<Book> books);

    List<BookDtoWithoutCategoryIds> toDtoListWithoutCategories(List<Book> books);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        Set<Long> longSet = book.getCategories()
                .stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        bookDto.setCategoriesIds(longSet);
    }

    @Named("bookFromId")
    default Book bookFromId(Long id) {
        if (id == null) {
            return null;
        }
        Book book = new Book();
        book.setId(id);
        return book;
    }
}
