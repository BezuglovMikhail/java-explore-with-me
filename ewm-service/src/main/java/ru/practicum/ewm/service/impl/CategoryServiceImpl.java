package ru.practicum.ewm.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.CustomPageRequest;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;
import ru.practicum.ewm.exeption.IncorrectParameterException;
import ru.practicum.ewm.exeption.NotFoundException;
import ru.practicum.ewm.exeption.ValidationException;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.service.CategoryService;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository repository;

    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public CategoryDto save(NewCategoryDto newCategoryDto) {
        try {
            return CategoryMapper.toCategoryDto(repository.save(CategoryMapper.toCategory(newCategoryDto)));
        } catch (ConstraintViolationException e) {
            throw new ValidationException("Validation exception");
        }
    }

    @Transactional
    @Override
    public void deleteCategory(long catId) {
        repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category whit id = " + catId + " not found in database."));
        repository.deleteById(catId);
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, long catId) {
        categoryDto.setId(catId);
        Category oldCategory = repository.findByName(categoryDto.getName());
        if (Objects.isNull(oldCategory)
                || categoryDto.getName().equals(oldCategory.getName())
                || oldCategory.getName().isBlank()) {
            repository.findById(catId)
                    .orElseThrow(() -> new NotFoundException("Category whit id = " + catId + " not found in database."));

            return CategoryMapper.toCategoryDto(repository.save(CategoryMapper.toCategoryUpdate(categoryDto, catId)));

        } else {
            throw new IncorrectParameterException("Incorrect parameter");
        }
    }

    @Override
    public List<CategoryDto> findCategories(Integer from, Integer size) {
        Sort sort = Sort.by("id").ascending();
        CustomPageRequest pageable = CustomPageRequest.by(from, size, sort);
        Page<Category> page = repository.findAll(pageable);

        return CategoryMapper.mapToCategoryDto(page.getContent());
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        return CategoryMapper.toCategoryDto(repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category whit id = " + catId + " not found in database.")));
    }
}
