package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class NewCompilationDto {

    private boolean pinned;

    @NotBlank
    @Size(min = 1, max = 50)
    private String title;

    @JsonProperty("events")
    private Set<Long> events;
}
