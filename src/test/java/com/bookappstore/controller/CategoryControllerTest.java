package com.bookappstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookappstore.dto.category.CategoryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.SQLException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:database/test/categories/add-categories-to-table.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/test/books/delete-books-categories.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "classpath:database/test/categories/delete-categories.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CategoryControllerTest {

    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create Category")
    void createCategory_ValidRequest_ShouldSaveCategory_Ok() throws Exception {
        CategoryDto createCategoryDto = new CategoryDto()
                .setName("New Category")
                .setDescription("Category description");

        String jsonRequest = objectMapper.writeValueAsString(createCategoryDto);

        MvcResult result = mockMvc.perform(
                        post("/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actualDto = objectMapper.readValue(result.getResponse()
                        .getContentAsString(),
                CategoryDto.class);
        Assertions.assertEquals(createCategoryDto
                        .getName(),
                actualDto.getName());
        Assertions.assertEquals(createCategoryDto
                        .getDescription(),
                actualDto.getDescription());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get all categories")
    void getAllCategories_ShouldReturnCategoriesList_Ok() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto[] actual = objectMapper.readValue(result.getResponse()
                        .getContentAsString(),
                CategoryDto[].class);
        Assertions.assertTrue(actual.length > 0);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Update Category")
    void updateCategory_ValidId_ShouldReturnUpdatedCategory_Ok() throws Exception {
        long categoryId = 12L;
        CategoryDto updateCategoryDto = new CategoryDto()
                .setName("Updated Category")
                .setDescription("Updated description");

        String jsonRequest = objectMapper.writeValueAsString(updateCategoryDto);

        MvcResult result = mockMvc.perform(
                        put("/categories/" + categoryId)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actualDto = objectMapper.readValue(result.getResponse()
                        .getContentAsString(),
                CategoryDto.class);
        Assertions.assertEquals(updateCategoryDto.getName(), actualDto.getName());
        Assertions.assertEquals(updateCategoryDto.getDescription(), actualDto.getDescription());
    }
}
