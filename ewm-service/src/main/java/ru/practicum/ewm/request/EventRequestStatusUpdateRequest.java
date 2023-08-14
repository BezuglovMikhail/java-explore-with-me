package ru.practicum.ewm.request;

import lombok.*;
import ru.practicum.ewm.until.status.State;

import java.util.List;

@Getter
@Setter
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;

    private State status;
}
