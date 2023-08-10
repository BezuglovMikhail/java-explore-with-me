package ru.practicum.ewm.dto;

import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {

    private Long id;

    @Size(min = 1, max = 50)
    private String name;
}
