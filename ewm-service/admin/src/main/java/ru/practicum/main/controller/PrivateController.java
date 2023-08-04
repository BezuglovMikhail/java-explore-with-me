package ru.practicum.main.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.EventFullDto;
import ru.practicum.main.dto.EventShortDto;
import ru.practicum.main.dto.NewEventDto;
import ru.practicum.main.dto.ParticipationRequestDto;
import ru.practicum.main.request.EventRequestStatusUpdateRequest;
import ru.practicum.main.request.EventRequestStatusUpdateResult;
import ru.practicum.main.request.UpdateEventAdminRequest;
import ru.practicum.main.service.EventService;
import ru.practicum.main.service.ParticipationRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}")
@Slf4j
public class PrivateController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ParticipationRequestService partRequestService;


    public PrivateController(EventService eventService, ParticipationRequestService partRequestService) {
        this.eventService = eventService;
        this.partRequestService = partRequestService;
    }

    @PostMapping("/events")
    @ResponseStatus(value = HttpStatus.CREATED)
    public EventFullDto saveEvent(@RequestBody NewEventDto newEventDto,
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
    public EventFullDto updateEvent(@RequestBody UpdateEventAdminRequest updateEventAdminRequest,
                                    @PathVariable("userId") Long userId, @RequestParam("eventId") Long eventId) {
        EventFullDto updateEvent = eventService.updateEvent(updateEventAdminRequest, eventId);
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
    public EventRequestStatusUpdateResult updateRequest(@RequestBody EventRequestStatusUpdateRequest updateRequest,
                                                        @PathVariable("userId") Long userId,
                                                        @RequestParam("eventId") Long eventId) {
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
                                                            @RequestParam("eventId") Long eventId) {
        ParticipationRequestDto addParticipationRequest = partRequestService.save(userId, eventId);
        log.info("Request Post received to add participationRequest user`s whit id = {}", userId);
        return addParticipationRequest;
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable("userId") Long userId,
                                                              @RequestParam("requestId") Long requestId) {
        ParticipationRequestDto cancelRequest = partRequestService.cancelRequest(userId, requestId);
        log.info("Request Post received to cancel participationRequest user`s whit id = {}", userId);
        return cancelRequest;
    }


}
