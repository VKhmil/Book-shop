package com.bookappstore.dto;

import com.bookappstore.validation.Description;
import com.bookappstore.validation.Isbn;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    @Isbn
    private String isbn;
    @NotNull
    @Min(0)
    private BigDecimal price;
    @NotBlank
    @Description
    private String description;
    private String coverImage;
}
