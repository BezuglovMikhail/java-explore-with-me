package ru.practicum.ewm.until;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.until.status.StateEvent;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SearchFilter {

    private List<Long> users;

    private List<StateEvent> states;

    private List<Long> categories;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeEnd;

    private String text;

    private Boolean paid;

    private Boolean onlyAvailable;
}
