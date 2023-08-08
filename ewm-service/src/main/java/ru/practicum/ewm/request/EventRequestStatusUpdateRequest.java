package ru.practicum.ewm.request;

import lombok.Data;
import ru.practicum.ewm.status.State;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;

    @Enumerated(EnumType.STRING)
    private State status;
}
