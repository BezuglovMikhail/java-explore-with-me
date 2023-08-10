package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.status.State;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ParticipationRequestDto {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    private Long event;

    private Long requester;

    private State status;
}
