package ru.practicum.ewm.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
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
            throw new ValidationException(e.getClass().getName(), e.getMessage(), e.getLocalizedMessage(),
                    HttpStatus.BAD_REQUEST, LocalDateTime.now());
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
        repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category whit id = " + catId + " not found in database."));
        categoryDto.setId(catId);
        Category oldCategory = repository.findByName(categoryDto.getName());
        if (Objects.isNull(oldCategory) || categoryDto.getName().equals(oldCategory.getName())) {

            return CategoryMapper.toCategoryDto(repository.save(CategoryMapper.toCategoryUpdate(categoryDto, catId)));

        } else {
            throw new IncorrectParameterException(getClass().getName(), "Integrity constraint has been violated.",
                    "could not execute statement; SQL [n/a]; constraint [uq_category_name];" +
                            " nested exception is org.hibernate.exception.ConstraintViolationException:" +
                            " could not execute statement",
                    HttpStatus.CONFLICT, LocalDateTime.now());
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
