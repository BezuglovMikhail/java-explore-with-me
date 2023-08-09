package ru.practicum.ewm.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.exeption.IncorrectParameterException;
import ru.practicum.ewm.exeption.NotFoundException;
import ru.practicum.ewm.exeption.ValidationException;
import ru.practicum.ewm.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.ParticipationRequest;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipationRequestRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.service.ParticipationRequestService;
import ru.practicum.ewm.status.State;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Transactional
    @Override
    public ParticipationRequestDto save(Long userId, Long eventId) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User whit id = " + userId + " not found in database."));
        ParticipationRequest request = new ParticipationRequest();
        if (!Objects.isNull(eventId)) {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new NotFoundException("Event whit id = " + eventId + " not found in database."));
            ParticipationRequest checkRequest = partRequestRepository.findByRequester_IdAndEvent_Id(userId, eventId);
            if (userId.equals(event.getInitiator().getId())
                    || !event.getState().equals(State.PUBLISHED)
                    || (event.getParticipantLimit() == event.getConfirmedRequests()
                    && event.getParticipantLimit() != 0)
                    || !Objects.isNull(checkRequest)) {

                throw new IncorrectParameterException(getClass().getName(), "User with id = " + userId +
                        " don`t have request",
                        "could not execute statement; SQL [n/a]; constraint [uq_category_name];" +
                                " nested exception is org.hibernate.exception.ConstraintViolationException:" +
                                " could not execute statement",
                        HttpStatus.CONFLICT, LocalDateTime.now());
            }

            request = partRequestRepository.save(ParticipationRequestMapper.toParticipationRequest(event, initiator));
            if (!event.getRequestModeration()) {
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                eventRepository.save(event);
            }
        } else {
            throw new ValidationException(getClass().getName(), "User with id = " + userId +
                    " don`t have request",
                    "could not execute statement; SQL [n/a]; constraint [uq_category_name];" +
                            " nested exception is org.hibernate.exception.ConstraintViolationException:" +
                            " could not execute statement",
                    HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        return ParticipationRequestMapper.toParticipationRequestDto(request);
    }

    @Override
    public List<ParticipationRequestDto> findAll(long userId) {
        return ParticipationRequestMapper.mapToParticipationRequestDto(
                partRequestRepository.findAllByRequester_id(userId));
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        ParticipationRequest oldRequest = partRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("ParticipationRequest whit id = " + requestId +
                        " not found in database."));

        if (userId.equals(oldRequest.getRequester().getId())
                && !oldRequest.getState().equals(State.PUBLISHED)) {
            oldRequest.setState(State.CANCELED);
            return ParticipationRequestMapper.toParticipationRequestDto(partRequestRepository.save(oldRequest));
        } else {
            throw new IncorrectParameterException(getClass().getName(), "User with id = " + userId +
                    " don`t have request with id = " + requestId,
                    "could not execute statement; SQL [n/a]; constraint [uq_category_name];" +
                            " nested exception is org.hibernate.exception.ConstraintViolationException:" +
                            " could not execute statement",
                    HttpStatus.CONFLICT, LocalDateTime.now());
        }
    }

    private void checkRequestForCancel(Long userId, Long requesterId, State oldRequestState) {
        if (userId.equals(requesterId)
                && !oldRequestState.equals(State.PUBLISHED)) {
            return;
        } else {
            throw new IncorrectParameterException(getClass().getName(), "User with id = " + userId +
                    " don`t have request with id = ",
                    "could not execute statement; SQL [n/a]; constraint [uq_category_name];" +
                            " nested exception is org.hibernate.exception.ConstraintViolationException:" +
                            " could not execute statement",
                    HttpStatus.CONFLICT, LocalDateTime.now());
        }
    }

    @Override
    public List<ParticipationRequestDto> findAllRequestEventByUserId(Long userId, Long eventId) {
        return ParticipationRequestMapper.mapToParticipationRequestDto(
                partRequestRepository.findAllByEvent_Id(eventId));
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult updateRequest(EventRequestStatusUpdateRequest updateRequest,
                                                        Long userId, Long eventId) {
        Event checkEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event whit id = " + eventId + " not found in database."));

        if (checkEvent.getParticipantLimit() <= 0
                || checkEvent.getConfirmedRequests() >= checkEvent.getParticipantLimit()
                || !checkEvent.getRequestModeration()) {
            throw new IncorrectParameterException(getClass().getName(), "Integrity constraint has been violated.",
                    "could not execute statement; SQL [n/a]; constraint [uq_category_name];" +
                            " nested exception is org.hibernate.exception.ConstraintViolationException:" +
                            " could not execute statement",
                    HttpStatus.CONFLICT, LocalDateTime.now());
        }
        List<ParticipationRequestDto> confirmed = new ArrayList<>();
        List<ParticipationRequestDto> rejected = new ArrayList<>();
        List<ParticipationRequest> requestList = partRequestRepository.findByIdIn(updateRequest.getRequestIds());

        for (ParticipationRequest request : requestList) {
            if (!request.getState().equals(State.PENDING)) {
                throw new IncorrectParameterException(getClass().getName(), "Integrity constraint has been violated.",
                        "could not execute statement; SQL [n/a]; constraint [uq_category_name];" +
                                " nested exception is org.hibernate.exception.ConstraintViolationException:" +
                                " could not execute statement",
                        HttpStatus.CONFLICT, LocalDateTime.now());
            }
            if (updateRequest.getStatus() == State.CONFIRMED
                    && checkEvent.getParticipantLimit() - checkEvent.getConfirmedRequests() > 0) {
                request.setState(State.CONFIRMED);
                checkEvent.setConfirmedRequests(checkEvent.getConfirmedRequests() + 1);
                confirmed.add(ParticipationRequestMapper.toParticipationRequestDto(request));
            } else {
                request.setState(State.REJECTED);
                rejected.add(ParticipationRequestMapper.toParticipationRequestDto(request));
            }
        }
        return new EventRequestStatusUpdateResult(confirmed, rejected);
    }
}
