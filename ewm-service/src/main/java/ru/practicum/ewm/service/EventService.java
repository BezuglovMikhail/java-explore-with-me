package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.NewEventDto;
import ru.practicum.ewm.request.UpdateEventAdminRequest;
import ru.practicum.ewm.request.UpdateEventUserRequest;
import ru.practicum.ewm.status.EventSort;
import ru.practicum.ewm.status.State;

import java.util.List;

public interface EventService {

    EventFullDto save(NewEventDto newEventDto, Long userId);

    EventFullDto getEventByIdForUserId(Long userId, Long eventId);

    EventFullDto getEventById(Long id);

    EventFullDto updateEventAdmin(UpdateEventAdminRequest updateCompilationRequest, long compId);

    EventFullDto updateEventPrivate(UpdateEventUserRequest updateEventUserRequest, long compId);

    void deleteEvent(long eventId);

    List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size);

    List<EventFullDto> adminFindEventsWhitFilter(List<Long> users, List<State> states, List<Long> categories,
                                            String rangeStartFormatter, String rangeEndFormatter,
                                            Integer from, Integer size);

    List<EventShortDto> publicFindEventsWhitFilter(String text, Boolean paid, Boolean onlyAvailable, EventSort sort,
                                                  List<Long> users, List<Long> categories,
                                                  String rangeStartFormatter, String rangeEndFormatter,
                                                  Integer from, Integer size);
}
