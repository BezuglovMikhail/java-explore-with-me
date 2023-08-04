package ru.practicum.main.service;

import ru.practicum.main.dto.ParticipationRequestDto;
import ru.practicum.main.request.EventRequestStatusUpdateRequest;
import ru.practicum.main.request.EventRequestStatusUpdateResult;

import java.util.List;

public interface ParticipationRequestService {

    ParticipationRequestDto save(Long userId, Long eventId);

    List<ParticipationRequestDto> findAll(long userId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> findAllRequestEventByUserId(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequest(EventRequestStatusUpdateRequest updateRequest, Long userId, Long eventId);
}
