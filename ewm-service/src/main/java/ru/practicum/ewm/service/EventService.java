package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.NewEventDto;
import ru.practicum.ewm.request.UpdateEventAdminRequest;
import ru.practicum.ewm.request.UpdateEventUserRequest;
import ru.practicum.ewm.until.status.EventSort;
import ru.practicum.ewm.until.status.StateEvent;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventFullDto save(NewEventDto newEventDto, Long userId);

    EventFullDto getEventByIdForUserId(Long userId, Long eventId);

    EventFullDto getEventById(Long id);

    EventFullDto updateEventAdmin(UpdateEventAdminRequest updateCompilationRequest, long compId);

    EventFullDto updateEventPrivate(UpdateEventUserRequest updateEventUserRequest, long compId);

    void deleteEvent(long eventId);

    List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size);

    List<EventFullDto> adminFindEventsWhitFilter(List<Long> users, List<StateEvent> states, List<Long> categories,
                                                 LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                 Integer from, Integer size);

    List<EventShortDto> publicFindEventsWhitFilter(String text, Boolean paid, Boolean onlyAvailable, EventSort sort,
                                                   List<Long> users, List<Long> categories,
                                                   LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                   Integer from, Integer size);
}
