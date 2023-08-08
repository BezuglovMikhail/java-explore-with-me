package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.NewEventDto;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.UpdateEventUserRequest;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.service.ParticipationRequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}")
@Slf4j
@RequiredArgsConstructor
public class PrivateController {

    private EventService eventService;

    private ParticipationRequestService partRequestService;

    @PostMapping("/events")
    @ResponseStatus(value = HttpStatus.CREATED)
    public EventFullDto saveEvent(@Valid @RequestBody NewEventDto newEventDto,
                                  @PathVariable("userId") Long userId) {
        EventFullDto addEvent = eventService.save(newEventDto, userId);
        log.info("Request Post received to add event user`s whit id = {}", userId);
        return addEvent;
    }

    @GetMapping("/events")
    public List<EventShortDto> getEventsByUserId(@PathVariable("userId") Long userId,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        List<EventShortDto> eventsUser = eventService.getEventsByUserId(userId, from, size);
        log.info("Request Get received to find all events user`s whit id = {}", userId);
        return eventsUser;
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getEventById(@PathVariable("userId") Long userId,
                                     @PathVariable("eventId") Long eventId) {
        EventFullDto eventDto = eventService.getEventByIdForUserId(userId, eventId);
        log.info("Request Get received to add event user`s whit id = {}", userId);
        return eventDto;
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@Valid @RequestBody UpdateEventUserRequest updateEventUserRequest,
                                    @PathVariable("userId") Long userId, @PathVariable("eventId") Long eventId) {
        EventFullDto updateEvent = eventService.updateEventPrivate(updateEventUserRequest, eventId);
        log.info("Request Patch received to update event whit id = {} user`s whit id = {}", eventId, userId);
        return updateEvent;
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getAllRequestEventByUserId(@PathVariable("userId") Long userId,
                                                                    @PathVariable("eventId") Long eventId) {
        List<ParticipationRequestDto> participationRequests = partRequestService
                .findAllRequestEventByUserId(userId, eventId);
        log.info("Request Get received to find all participationRequest user`s whit id = {}", userId);
        return participationRequests;
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequest(@Valid @RequestBody EventRequestStatusUpdateRequest updateRequest,
                                                        @PathVariable("userId") Long userId,
                                                        @PathVariable("eventId") Long eventId) {
        EventRequestStatusUpdateResult updateResult = partRequestService.updateRequest(updateRequest, userId, eventId);
        log.info("Request Patch received to update status request event whit id = {} user`s whit id = {}",
                eventId, userId);
        return updateResult;
    }

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getParticipationRequestCurrentUser(@PathVariable("userId") Long userId) {
        List<ParticipationRequestDto> participationRequests = partRequestService.findAll(userId);
        log.info("Request Get received to find all participationRequest user`s whit id = {}", userId);
        return participationRequests;
    }

    @PostMapping("/requests")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ParticipationRequestDto saveParticipationRequest(@PathVariable("userId") Long userId,
                                                            @RequestParam(required = false) Long eventId) {
        ParticipationRequestDto addParticipationRequest = partRequestService.save(userId, eventId);
        log.info("Request Post received to add participationRequest user`s whit id = {}", userId);
        return addParticipationRequest;
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable("userId") Long userId,
                                                              @PathVariable("requestId") Long requestId) {
        ParticipationRequestDto cancelRequest = partRequestService.cancelRequest(userId, requestId);
        log.info("Request Post received to cancel participationRequest user`s whit id = {}", userId);
        return cancelRequest;
    }
}
