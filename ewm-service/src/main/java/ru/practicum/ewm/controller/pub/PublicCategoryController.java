package ru.practicum.ewm.controller.pub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping
@Slf4j
public class PublicCategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        log.info("Request Get received to find list categories ");
        List<CategoryDto> categoryDtoList = categoryService.findCategories(from, size);
        return categoryDtoList;
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryDtoById(@PathVariable("catId") Long catId) {
        CategoryDto compilationDto = categoryService.getCategoryById(catId);
        log.info("Request Get received to category whit id = {}", catId);
        return compilationDto;
    }
}
