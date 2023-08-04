package ru.practicum.main.request;

import lombok.Data;
import ru.practicum.main.status.State;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;

    @Enumerated(EnumType.STRING)
    private State status;
}
