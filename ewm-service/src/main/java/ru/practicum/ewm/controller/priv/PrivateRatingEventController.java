package ru.practicum.ewm.controller.priv;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.RatingDto;
import ru.practicum.ewm.dto.RatingEventDto;
import ru.practicum.ewm.dto.newdto.NewRatingEventDto;
import ru.practicum.ewm.service.RatingEventService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users/{userId}")
@Slf4j
public class PrivateRatingEventController {

    @Autowired
    private RatingEventService ratingEventService;

    @PostMapping("/events/{eventId}/assessment")
    @ResponseStatus(value = HttpStatus.CREATED)
    public RatingEventDto saveRatingEvent(@PathVariable("userId") Long userId,
                                          @PathVariable("eventId") Long eventId,
                                          @Valid @RequestBody NewRatingEventDto newRatingEventDto) {
        RatingEventDto addRatingEventDto = ratingEventService.save(userId, eventId, newRatingEventDto);
        log.info("Request Post received to add assessment user`s whit id = {}, event whit id = {}", userId, eventId);
        return addRatingEventDto;
    }

    @GetMapping("/events/{eventId}/rating")
    public RatingDto getRatingEvent(@PathVariable("eventId") Long eventId) {
        RatingDto ratingEventShortDto = ratingEventService.getRatingById(eventId);
        log.info("Request Get received to find rating Event whit id = {}", eventId);
        return ratingEventShortDto;
    }

    @GetMapping("/rating")
    public RatingDto getRatingInitiator(@PathVariable("userId") Long userId) {
        RatingDto ratingEventShortDto = ratingEventService.getRatingByUserId(userId);
        log.info("Request Get received to find rating Initiator whit id = {}", userId);
        return ratingEventShortDto;
    }


    @DeleteMapping("/events/{eventId}/rating/{ratingId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteRating(@PathVariable long userId,
                             @PathVariable long eventId,
                             @PathVariable long ratingId) {
        ratingEventService.deleteRating(userId, eventId, ratingId);
        log.info("Request Delete received to rating users`s whit id = {}, event whit id = {} ", eventId, userId);
    }
}
