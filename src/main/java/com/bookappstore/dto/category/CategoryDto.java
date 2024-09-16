package com.bookappstore.dto.category;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CategoryDto {
    private String name;
    private String description;
}
