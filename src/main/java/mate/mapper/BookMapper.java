package mate.mapper;

import mate.config.MapperConfig;
import mate.dto.BookDto;
import mate.dto.CreateBookRequestDto;
import mate.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

}
