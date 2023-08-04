package ru.practicum.main.service;

import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto save(NewCategoryDto newCategoryDto);

    void deleteCategory(long userId);

    CategoryDto updateCategory(CategoryDto categoryDto, long catId);

    List<CategoryDto> findCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Long catId);
}
