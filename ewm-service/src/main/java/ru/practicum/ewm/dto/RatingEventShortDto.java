package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.mapper.UserMapper;
import ru.practicum.ewm.model.Event;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RatingEventShortDto {

    private Long id;

    private String annotation;

    private CategoryDto category;

    private Long confirmedRequests;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private UserShortDto initiator;

    private Boolean paid;

    private String title;

    private Long likes;

    private Long dislikes;

    public RatingEventShortDto(Event event, Long likes, Long dislikes) {
        this.id = event.getId();
        this.annotation = event.getAnnotation();
        this.category = CategoryMapper.toCategoryDto(event.getCategory());
        this.eventDate = event.getEventDate();
        this.initiator = UserMapper.toUserShortDto(event.getInitiator());
        this.paid = event.getPaid();
        this.title = event.getTitle();
        this.likes = likes;
        this.dislikes = dislikes;
    }
}
