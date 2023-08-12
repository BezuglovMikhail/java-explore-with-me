package ru.practicum.ewm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.until.ConfirmedRequests;
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
import ru.practicum.ewm.until.status.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    @Autowired
    private final ParticipationRequestRepository partRequestRepository;
    @Autowired
    private final EventRepository eventRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ConfirmedRequests confirmedRequests;

    public ParticipationRequestServiceImpl(ParticipationRequestRepository partRequestRepository,
                                           EventRepository eventRepository, UserRepository userRepository, ConfirmedRequests confirmedRequests) {
        this.partRequestRepository = partRequestRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.confirmedRequests = confirmedRequests;
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
            Integer checkConfirmedReq = confirmedRequests.findConfirmedRequests(List.of(event)).get(eventId);
            if (userId.equals(event.getInitiator().getId())
                    || !event.getState().equals(State.PUBLISHED)
                    || (event.getParticipantLimit().equals(checkConfirmedReq)
                    && event.getParticipantLimit() != 0)
                    || !Objects.isNull(checkRequest)) {

                throw new IncorrectParameterException("Incorrect parameter");
            }

            request = partRequestRepository.save(ParticipationRequestMapper.toParticipationRequest(event, initiator));
            if (!event.getRequestModeration()) {
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                eventRepository.save(event);
            }
        } else {
            throw new ValidationException("Validation exception");
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
            throw new IncorrectParameterException("Incorrect parameter");
        }
    }

    private void checkRequestForCancel(Long userId, Long requesterId, State oldRequestState) {
        if (userId.equals(requesterId)
                && !oldRequestState.equals(State.PUBLISHED)) {
            return;
        } else {
            throw new IncorrectParameterException("Incorrect parameter");
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
        Integer checkConfirmedReq = confirmedRequests.findConfirmedRequests(List.of(checkEvent)).get(eventId);
        if (checkEvent.getParticipantLimit() <= 0
                || checkConfirmedReq >= checkEvent.getParticipantLimit()
                || !checkEvent.getRequestModeration()) {
            throw new IncorrectParameterException("Incorrect parameter");
        }
        List<ParticipationRequestDto> confirmed = new ArrayList<>();
        List<ParticipationRequestDto> rejected = new ArrayList<>();
        List<ParticipationRequest> requestList = partRequestRepository.findByIdIn(updateRequest.getRequestIds());

        for (ParticipationRequest request : requestList) {
            if (!request.getState().equals(State.PENDING)) {
                throw new IncorrectParameterException("Incorrect parameter");
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
