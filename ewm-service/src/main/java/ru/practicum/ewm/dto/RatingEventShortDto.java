package ru.practicum.ewm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.Event;

@Getter
@Setter
@NoArgsConstructor
public class RatingEventShortDto {

    private EventShortDto event;

    private Long likes;

    private Long dislikes;

    public RatingEventShortDto(EventShortDto event, Long likes, Long dislikes) {
        this.event = event;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public RatingEventShortDto(Event event, Long likes, Long dislikes) {
        this.event = EventMapper.toEventShortDto(event);
        this.likes = likes;
        this.dislikes = dislikes;
    }
}
