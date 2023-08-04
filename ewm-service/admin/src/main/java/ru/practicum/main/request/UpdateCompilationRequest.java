package ru.practicum.main.request;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UpdateCompilationRequest {
    private List<Long> events;
    private boolean pinned;

    @Size(min = 1, max = 50)
    private String title;
}
