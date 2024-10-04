package com.bookappstore.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bookappstore.dto.category.CategoryDto;
import com.bookappstore.exception.EntityNotFoundException;
import com.bookappstore.mapper.CategoryMapper;
import com.bookappstore.model.Category;
import com.bookappstore.repository.category.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryDto categoryDto;

    @Test
    @DisplayName("Save category")
    void saveCategory_Valid_ShouldSaveCategory_Ok() {
        categoryDto = createCategoryDto();
        category = createCategory();

        when(categoryMapper.toEntity(categoryDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.save(categoryDto);

        assertEquals(categoryDto, result);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    @DisplayName("Find all categories")
    void findAll_ShouldReturnAllCategories_Ok() {
        Category categoryOne = createCategory();
        Category categoryTwo = createCategory();
        categoryTwo.setName("Valid name");
        CategoryDto categoryDtoOne = createCategoryDto();
        CategoryDto categoryDtoTwo = createCategoryDto();
        categoryDtoTwo.setName("Valid name");

        BeanUtils.copyProperties(categoryOne, categoryTwo);
        BeanUtils.copyProperties(categoryDtoOne, categoryDtoTwo);

        Pageable pageable = PageRequest.of(1, 2);
        Page<Category> page = new PageImpl<>(List.of(categoryOne, categoryTwo), pageable, 2);

        when(categoryRepository.findAll(pageable)).thenReturn(page);
        when(categoryMapper.toDto(categoryOne)).thenReturn(categoryDtoOne);
        when(categoryMapper.toDto(categoryTwo)).thenReturn(categoryDtoTwo);

        List<CategoryDto> actual = categoryService.findAll(pageable);

        assertEquals(List.of(categoryDtoOne, categoryDtoTwo), actual);
    }

    @Test
    @DisplayName("Get category by id")
    void getById_ShouldReturnValidCategory() {
        category = createCategory();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(createCategoryDto());

        CategoryDto result = categoryService.getById(1L);

        assertEquals(createCategoryDto(), result);
    }

    @Test
    @DisplayName("Throw exception, when category id don't valid")
    void getByIdNotFound_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            categoryService.getById(1L);
        });

        assertEquals("Can't find category by id: 1", exception.getMessage());
    }

    @Test
    @DisplayName("Update category")
    void update_Valid_ShouldUpdateCategory_Ok() {
        category = createCategory();
        categoryDto = createCategoryDto();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.update(1L, categoryDto);

        assertEquals(categoryDto, result);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    @DisplayName("Delete category by ID")
    void deleteById_ShouldDeleteCategory_Ok() {
        categoryService.deleteById(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    private Category createCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");
        category.setDescription("Test Description");
        return category;
    }

    private CategoryDto createCategoryDto() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Test Category");
        categoryDto.setDescription("Test Description");
        return categoryDto;
    }
}
