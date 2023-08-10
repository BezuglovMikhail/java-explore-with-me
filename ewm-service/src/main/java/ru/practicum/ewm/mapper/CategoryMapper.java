package ru.practicum.ewm.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CategoryMapper {

    public CategoryDto toCategoryDto(Category category) {
       CategoryDto categoryDto = new CategoryDto();
           categoryDto.setId(category.getId());
           categoryDto.setName(category.getName());
        return categoryDto;
    }

    public Category toCategory(NewCategoryDto newCategoryDto) {
        return new Category(
                newCategoryDto.getName()
        );
    }

    public Category toCategoryFull(CategoryDto categoryDto) {
        Category newCategory = new Category();
        newCategory.setName(categoryDto.getName());
        return newCategory;
    }

    public Category toCategoryUpdate(CategoryDto categoryDto, Long catId) {
        Category categoryUpdate = new Category();
        categoryUpdate.setId(catId);
        categoryUpdate.setName(categoryDto.getName());
        return categoryUpdate;
    }

    public List<CategoryDto> mapToCategoryDto(Iterable<Category> categories) {
        List<CategoryDto> result = new ArrayList<>();

        for (Category category : categories) {
            result.add(toCategoryDto(category));
        }
        return result;
    }
}
