package ru.practicum.ewm.controller.priv;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.newdto.NewEventDto;
import ru.practicum.ewm.request.UpdateEventUserRequest;
import ru.practicum.ewm.service.EventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@Slf4j
public class PrivateEventController {

    @Autowired
    private EventService eventService;


    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public EventFullDto saveEvent(@Valid @RequestBody NewEventDto newEventDto,
                                  @PathVariable("userId") Long userId) {
        EventFullDto addEvent = eventService.save(newEventDto, userId);
        log.info("Request Post received to add event user`s whit id = {}", userId);
        return addEvent;
    }

    @GetMapping
    public List<EventShortDto> getEventsByUserId(@PathVariable("userId") Long userId,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        List<EventShortDto> eventsUser = eventService.getEventsByUserId(userId, from, size);
        log.info("Request Get received to find all events user`s whit id = {}", userId);
        return eventsUser;
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable("userId") Long userId,
                                     @PathVariable("eventId") Long eventId) {
        EventFullDto eventDto = eventService.getEventByIdForUserId(userId, eventId);
        log.info("Request Get received to add event user`s whit id = {}", userId);
        return eventDto;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@Valid @RequestBody UpdateEventUserRequest updateEventUserRequest,
                                    @PathVariable("userId") Long userId, @PathVariable("eventId") Long eventId) {
        EventFullDto updateEvent = eventService.updateEventPrivate(updateEventUserRequest, eventId);
        log.info("Request Patch received to update event whit id = {} user`s whit id = {}", eventId, userId);
        return updateEvent;
    }
}
