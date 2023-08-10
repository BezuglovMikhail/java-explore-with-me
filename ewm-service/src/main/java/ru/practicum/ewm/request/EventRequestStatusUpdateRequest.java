package ru.practicum.ewm.request;

import lombok.Data;
import ru.practicum.ewm.status.State;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;

    private State status;
}
