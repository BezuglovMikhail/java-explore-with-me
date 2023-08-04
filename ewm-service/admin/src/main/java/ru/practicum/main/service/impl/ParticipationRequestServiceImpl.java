package ru.practicum.main.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.main.dto.ParticipationRequestDto;
import ru.practicum.main.exeption.NotFoundException;
import ru.practicum.main.exeption.ValidationException;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.ParticipationRequest;
import ru.practicum.main.model.User;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.ParticipationRequestRepository;
import ru.practicum.main.repository.UserRepository;
import ru.practicum.main.request.EventRequestStatusUpdateRequest;
import ru.practicum.main.request.EventRequestStatusUpdateResult;
import ru.practicum.main.service.ParticipationRequestService;
import ru.practicum.main.status.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ru.practicum.main.mapper.ParticipationRequestMapper.*;

@Service
@Slf4j
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    @Autowired
    private final ParticipationRequestRepository partRequestRepository;
    @Autowired
    private final EventRepository eventRepository;
    @Autowired
    private final UserRepository userRepository;

    public ParticipationRequestServiceImpl(ParticipationRequestRepository partRequestRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.partRequestRepository = partRequestRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ParticipationRequestDto save(Long userId, Long eventId) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User whit id = " + userId + " not found in database."));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event whit id = " + eventId + " not found in database."));

        return toParticipationRequestDto(partRequestRepository.save(toParticipationRequest(event, initiator)));
    }

    @Override
    public List<ParticipationRequestDto> findAll(long userId) {
        return mapToParticipationRequestDto(partRequestRepository.findAllByRequester_id(userId));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {

        ParticipationRequest oldRequest = partRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("ParticipationRequest whit id = " + requestId +
                        " not found in database."));

        if (Objects.equals(oldRequest.getRequester().getId(), userId)) {
            oldRequest.setStatus(State.CANCELED);
            return toParticipationRequestDto(partRequestRepository.save(oldRequest));
        } else {
            throw new ValidationException(getClass().getName(), "User with id = " + userId +
                    " don`t have request with id = " + requestId,
                    "could not execute statement; SQL [n/a]; constraint [uq_category_name];" +
                            " nested exception is org.hibernate.exception.ConstraintViolationException:" +
                            " could not execute statement",
                    HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
    }

    @Override
    public List<ParticipationRequestDto> findAllRequestEventByUserId(Long userId, Long eventId) {

        return mapToParticipationRequestDto(partRequestRepository.findAllByRequester_idAndEvent_id(userId, eventId));
    }

    @Override
    public EventRequestStatusUpdateResult updateRequest(EventRequestStatusUpdateRequest updateRequest,
                                                        Long userId, Long eventId) {
        List<ParticipationRequest> requestList = partRequestRepository.findByIdIn(updateRequest.getRequestIds());
        for (ParticipationRequest request : requestList) {
            request.setStatus(updateRequest.getStatus());
        }
        partRequestRepository.saveAll(requestList);

        EventRequestStatusUpdateResult updateResult = new EventRequestStatusUpdateResult();

        updateResult.setConfirmedRequests(mapToParticipationRequestDto(partRequestRepository
                .findAllByRequester_idAndEvent_idAndStatus(userId, eventId, State.CONFIRMED.toString())));
        updateResult.setConfirmedRequests(mapToParticipationRequestDto(partRequestRepository
                .findAllByRequester_idAndEvent_idAndStatus(userId, eventId, State.REJECTED.toString())));

        return updateResult;
    }


}
