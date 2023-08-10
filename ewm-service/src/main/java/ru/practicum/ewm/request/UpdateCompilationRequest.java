package ru.practicum.ewm.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class UpdateCompilationRequest {

    private Set<Long> events;

    private Boolean pinned;

    @Size(min = 1, max = 50)
    private String title;
}
