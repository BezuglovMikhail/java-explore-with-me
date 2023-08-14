package ru.practicum.ewm.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CompilationDto {

    private Long id;

    private Boolean pinned;

    private String title;

    private List<EventShortDto> events;
}
