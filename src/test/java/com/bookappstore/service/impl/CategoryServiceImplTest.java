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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Ініціалізація тестових даних
        category = new Category();
        category.setId(1L);
        category.setName("Test Category");
        category.setDescription("Test Description");

        categoryDto = new CategoryDto();
        categoryDto.setName("Test Category");
        categoryDto.setDescription("Test Description");
    }

    @Test
    void testSave() {
        when(categoryMapper.toEntity(categoryDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.save(categoryDto);

        assertEquals(categoryDto, result);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(List.of(category));

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        List<CategoryDto> result = categoryService.findAll(pageable);

        assertEquals(1, result.size());
        assertEquals(categoryDto, result.get(0));
    }

    @Test
    void testGetById() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getById(1L);

        assertEquals(categoryDto, result);
    }

    @Test
    void testGetByIdNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            categoryService.getById(1L);
        });

        assertEquals("Can't find category by id: 1", exception.getMessage());
    }

    @Test
    void testUpdate() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.update(1L, categoryDto);

        assertEquals(categoryDto, result);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testDeleteById() {
        categoryService.deleteById(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }
}
