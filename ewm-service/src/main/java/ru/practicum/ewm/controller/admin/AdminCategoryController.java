package ru.practicum.ewm.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;
import ru.practicum.ewm.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/categories")
@Slf4j
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        CategoryDto addedCategory = categoryService.save(newCategoryDto);
        log.info("Request Post received to add category: {}", addedCategory);
        return addedCategory;
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long catId) {
        categoryService.deleteCategory(catId);
        log.info("Request Delete received to category delete, categoryDeleteId = {} ", catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable Long catId) {
        CategoryDto updatedCategory = categoryService.updateCategory(categoryDto, catId);
        log.info("Request Update received to update user, updateUser: {}", updatedCategory);
        return updatedCategory;
    }
}
