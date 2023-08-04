package ru.practicum.main.request;

import lombok.Data;
import ru.practicum.main.status.State;
import ru.practicum.main.model.Location;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class UpdateEventAdminRequest {

    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    private LocalDateTime eventDate;
    private Location location;
    private boolean paid;

    private Integer participantLimit;

    private boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private State stateAction;

    @Size(min = 3, max = 120)
    private String title;
}
