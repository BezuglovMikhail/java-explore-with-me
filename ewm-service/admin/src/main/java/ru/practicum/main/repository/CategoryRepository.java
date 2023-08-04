package ru.practicum.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main.dto.NewCategoryDto;
import ru.practicum.main.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    NewCategoryDto findAllByName(String nameSearch);
}
