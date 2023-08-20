package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.RatingDto;
import ru.practicum.ewm.dto.RatingEventDto;
import ru.practicum.ewm.dto.RatingEventShortDto;
import ru.practicum.ewm.dto.newdto.NewRatingEventDto;
import ru.practicum.ewm.until.status.StateAssessment;

import java.util.List;

public interface RatingEventService {

    RatingEventDto save(Long userId, Long eventId, NewRatingEventDto newRatingEventDto);

    RatingDto getRatingById(Long eventId);

    RatingDto getRatingByUserId(Long userId);

    void deleteRating(Long userId, Long eventId, Long ratingId);

    List<RatingEventShortDto> findEventsWhitRating(StateAssessment assessment, Integer from, Integer size);
}
