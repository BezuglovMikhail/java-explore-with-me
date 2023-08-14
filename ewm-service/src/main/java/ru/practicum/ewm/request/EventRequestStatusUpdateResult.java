package ru.practicum.ewm.request;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.dto.ParticipationRequestDto;

import java.util.List;

@Getter
@Setter
public class EventRequestStatusUpdateResult {

    private List<ParticipationRequestDto> confirmedRequests;

    private List<ParticipationRequestDto> rejectedRequests;

    public EventRequestStatusUpdateResult(List<ParticipationRequestDto> confirmedRequests,
                                          List<ParticipationRequestDto> rejectedRequests) {
        this.confirmedRequests = confirmedRequests;
        this.rejectedRequests = rejectedRequests;
    }
}
