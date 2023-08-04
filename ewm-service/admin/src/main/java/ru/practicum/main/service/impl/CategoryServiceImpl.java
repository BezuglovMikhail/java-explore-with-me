package ru.practicum.main.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.main.CustomPageRequest;
import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.dto.NewCategoryDto;
import ru.practicum.main.exeption.NotFoundException;
import ru.practicum.main.exeption.ValidationException;
import ru.practicum.main.model.Category;
import ru.practicum.main.repository.CategoryRepository;
import ru.practicum.main.service.CategoryService;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.main.mapper.CategoryMapper.*;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository repository;

    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public CategoryDto save(NewCategoryDto newCategoryDto) {
        try {
            return toCategoryDto(repository.save(toCategory(newCategoryDto)));
        } catch (ConstraintViolationException e) {
            throw new ValidationException(e.getClass().getName(), e.getMessage(), e.getMessage(),
                    HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
    }

    @Override
    public void deleteCategory(long catId) {
        repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category whit id = " + catId + " not found in database."));
        repository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, long catId) {
        repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category whit id = " + catId + " not found in database."));
        categoryDto.setId(catId);
        if (repository.findAllByName(categoryDto.getName()) == null) {
            return toCategoryDto(repository.save(toCategoryUpdate(categoryDto)));
            //return toCategoryDto(updateCategory(newCategoryDto.getName(), catId));
        } else {
            throw new ValidationException(getClass().getName(), "Integrity constraint has been violated.",
                    "could not execute statement; SQL [n/a]; constraint [uq_category_name];" +
                            " nested exception is org.hibernate.exception.ConstraintViolationException:" +
                            " could not execute statement",
                    HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

    }

    @Override
    public List<CategoryDto> findCategories(Integer from, Integer size) {
        Sort sort = Sort.by("id").ascending();
        CustomPageRequest pageable = CustomPageRequest.by(from, size, sort);
        Page<Category> page = repository.findAll(pageable);

        return mapToCategoryDto(page.getContent());
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        return toCategoryDto(repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category whit id = " + catId + " not found in database.")));
    }

   /* @Modifying
    @Query("update Category cat set cat.name = :nameCategory where cat.id = :catId")
    public Category updateCategory(String nameCategory, long catId) {
        return new Category(catId, nameCategory);
    }*/

}
