package ru.practicum.main.service;

import ru.practicum.main.dto.EventFullDto;
import ru.practicum.main.dto.EventShortDto;
import ru.practicum.main.dto.NewEventDto;
import ru.practicum.main.request.UpdateEventAdminRequest;
import ru.practicum.main.status.EventSort;
import ru.practicum.main.status.State;

import java.util.List;

public interface EventService {

    EventFullDto save(NewEventDto newEventDto, Long userId);

    EventFullDto getEventByIdForUserId(Long userId, Long eventId);

    EventFullDto getEventById(Long id);

    EventFullDto updateEvent(UpdateEventAdminRequest updateCompilationRequest, long compId);

    void deleteEvent(long eventId);

    List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size);

    List<EventFullDto> adminFindEventsWhitFilter(List<Long> users, List<State> states, List<Long> categories,
                                            String rangeStartFormatter, String rangeEndFormatter,
                                            Integer from, Integer size);

    List<EventFullDto> publicFindEventsWhitFilter(String text, Boolean paid, Boolean onlyAvailable, EventSort sort,
                                                  List<Long> users, List<State> states, List<Long> categories,
                                                  String rangeStartFormatter, String rangeEndFormatter,
                                                  Integer from, Integer size);
}
