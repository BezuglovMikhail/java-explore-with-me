package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto save(NewCategoryDto newCategoryDto);

    void deleteCategory(long userId);

    CategoryDto updateCategory(CategoryDto categoryDto, long catId);

    List<CategoryDto> findCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Long catId);
}
