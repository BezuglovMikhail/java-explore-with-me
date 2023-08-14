package ru.practicum.ewm.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.request.UpdateEventAdminRequest;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.until.status.StateEvent;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@Slf4j
public class AdminEventController {

    @Autowired
    private EventService eventService;

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest,
                                    @PathVariable Long eventId) {
        EventFullDto eventFullDtoUpdate = eventService.updateEventAdmin(updateEventAdminRequest, eventId);
        log.info("Request Update received to update event whit id = {}", eventId);
        return eventFullDtoUpdate;
    }

    @DeleteMapping("/{eventId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        log.info("Request Delete received to delete event whit id = {}", eventId);
    }

    @GetMapping
    public List<EventFullDto> getEventsWhitFilters(@RequestParam(required = false) List<Long> users,
                                                   @RequestParam(required = false) List<StateEvent> states,
                                                   @RequestParam(required = false) List<Long> categories,
                                                   @RequestParam(required = false)
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                   LocalDateTime rangeStart,
                                                   @RequestParam(required = false)
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                   LocalDateTime rangeEnd,
                                                   @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(defaultValue = "10") Integer size) {

        log.debug("Request Get received whit parameters: " +
                        "userIds: {}, states: {}, categoryIds: {}, rangeStart: {}, rangeEnd {}, from: {}, size: {}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.adminFindEventsWhitFilter(users, states, categories, rangeStart,
                rangeEnd, from, size);
    }
}
