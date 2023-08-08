package ru.practicum.ewm.mapper;

import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {

    private CategoryMapper() {
    }

    public static CategoryDto toCategoryDto(Category category) {
       CategoryDto categoryDto = new CategoryDto();
           categoryDto.setId(category.getId());
           categoryDto.setName(category.getName());
        return categoryDto;
    }

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return new Category(
                newCategoryDto.getName()
        );
    }

    public static Category toCategoryFull(CategoryDto categoryDto) {
        Category newCategory = new Category();
        newCategory.setName(categoryDto.getName());
        return newCategory;
    }

    public static Category toCategoryUpdate(CategoryDto categoryDto, Long catId) {
        Category categoryUpdate = new Category();
        categoryUpdate.setId(catId);
        categoryUpdate.setName(categoryDto.getName());
        return categoryUpdate;
    }

    public static List<CategoryDto> mapToCategoryDto(Iterable<Category> categories) {
        List<CategoryDto> result = new ArrayList<>();

        for (Category category : categories) {
            result.add(toCategoryDto(category));
        }
        return result;
    }
}
