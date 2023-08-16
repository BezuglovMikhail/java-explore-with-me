package ru.practicum.ewm.dto.newdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.until.status.StateAssessment;

@Getter
@Setter
@NoArgsConstructor
public class NewRatingEventDto {

    private StateAssessment state;
}
