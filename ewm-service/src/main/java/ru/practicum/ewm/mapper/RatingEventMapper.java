package ru.practicum.ewm.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.dto.RatingEventDto;
import ru.practicum.ewm.dto.newdto.NewRatingEventDto;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.RatingEvent;
import ru.practicum.ewm.model.User;

@UtilityClass
public class RatingEventMapper {

    public RatingEvent toRatingEvent(User appraiser, Event event, NewRatingEventDto newRatingEventDto) {
        RatingEvent ratingEvent = new RatingEvent();
        ratingEvent.setAppraiser(appraiser);
        ratingEvent.setEvent(event);
        ratingEvent.setState(newRatingEventDto.getState());
        return ratingEvent;
    }

    public RatingEventDto toRatingEventDto(RatingEvent ratingEvent, Long confirmedReq) {
        RatingEventDto ratingEventDto = new RatingEventDto();
        ratingEventDto.setId(ratingEvent.getId());
        ratingEventDto.setAppraiser(UserMapper.toUserShortDto(ratingEvent.getAppraiser()));
        ratingEventDto.setEvent(EventMapper.toEventShortDto(ratingEvent.getEvent(), confirmedReq));
        ratingEventDto.setState(ratingEvent.getState());
        return ratingEventDto;
    }
}
