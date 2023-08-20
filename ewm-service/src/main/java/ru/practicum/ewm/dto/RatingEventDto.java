package ru.practicum.ewm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.until.status.StateAssessment;

@Getter
@Setter
@NoArgsConstructor
public class RatingEventDto {

    private Long id;

    private UserShortDto appraiser;

    private EventShortDto event;

    private StateAssessment state;
}
