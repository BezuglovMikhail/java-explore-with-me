package ru.practicum.ewm.controller.pub;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.RatingEventShortDto;
import ru.practicum.ewm.service.RatingEventService;
import ru.practicum.ewm.until.status.StateAssessment;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/events/rating")
@Slf4j
public class PublicRatingEventController {

    @Autowired
    private RatingEventService ratingEventService;

    @GetMapping
    public List<RatingEventShortDto> getEventsWhitRating(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                         @Positive @RequestParam(defaultValue = "10") Integer size,
                                                         @RequestParam(name = "assessment",
                                                                 defaultValue = "LIKE") StateAssessment assessment) {
        log.info("Request Get received whit parameter assessment = {} to find list compilations ", assessment);
        List<RatingEventShortDto> eventsWhitRating = ratingEventService.findEventsWhitRating(assessment, from, size);
        return eventsWhitRating;
    }
}
