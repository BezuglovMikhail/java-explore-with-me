package ru.practicum.ewm.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.RatingDto;
import ru.practicum.ewm.dto.RatingEventDto;
import ru.practicum.ewm.dto.RatingEventShortDto;
import ru.practicum.ewm.dto.newdto.NewRatingEventDto;
import ru.practicum.ewm.exeption.IncorrectParameterException;
import ru.practicum.ewm.exeption.NotFoundException;
import ru.practicum.ewm.exeption.ValidationException;
import ru.practicum.ewm.mapper.RatingEventMapper;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.ParticipationRequest;
import ru.practicum.ewm.model.RatingEvent;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipationRequestRepository;
import ru.practicum.ewm.repository.RatingEventRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.RatingEventService;
import ru.practicum.ewm.until.ConfirmedRequests;
import ru.practicum.ewm.until.CustomPageRequest;
import ru.practicum.ewm.until.status.StateAssessment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RatingEventServiceImp implements RatingEventService {

    private final RatingEventRepository ratingEventRepository;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final ParticipationRequestRepository partRequestRepository;

    private final ConfirmedRequests confirmedRequests;

    public RatingEventServiceImp(RatingEventRepository ratingEventRepository, EventRepository eventRepository,
                                 UserRepository userRepository, ParticipationRequestRepository partRequestRepository, ConfirmedRequests confirmedRequests) {
        this.ratingEventRepository = ratingEventRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.partRequestRepository = partRequestRepository;
        this.confirmedRequests = confirmedRequests;
    }

    @Override
    public RatingEventDto save(Long userId, Long eventId, NewRatingEventDto newRatingEventDto) {
        User appraiser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User whit id = " + userId + " not found in database."));
        Event checkEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event whit id = " + eventId + " not found in database."));
        ParticipationRequest checkRequest = partRequestRepository.findByRequester_IdAndEvent_Id(userId, eventId);

        if (checkRequest != null && checkEvent.getEventDate().isBefore(LocalDateTime.now())) {
            Long confirmedReq = confirmedRequests.findCountRequests(eventId);
            if (confirmedReq == null) {
                confirmedReq = 0L;
            }
            return RatingEventMapper.toRatingEventDto(ratingEventRepository.save(
                    RatingEventMapper.toRatingEvent(appraiser, checkEvent, newRatingEventDto)), confirmedReq);
        } else {
            throw new IncorrectParameterException("Event hasn't happened yet or user did not participate in the event");
        }
    }

    @Override
    public RatingDto getRatingById(Long eventId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event whit id = " + eventId + " not found in database."));
        return ratingEventRepository.findRating(eventId);
    }

    @Override
    public RatingDto getRatingByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Event whit id = " + userId + " not found in database."));
        return ratingEventRepository.findUserRating(userId);
    }

    @Override
    public void deleteRating(Long userId, Long eventId, Long ratingId) {
        RatingEvent checkRating = ratingEventRepository.findById(ratingId)
                .orElseThrow(() -> new NotFoundException("Rating whit id = " + ratingId + " not found in database."));
        if (checkRating.getAppraiser().getId().equals(userId) && checkRating.getEvent().getId().equals(eventId)) {
            ratingEventRepository.deleteById(ratingId);
        } else {
            throw new ValidationException("User whit id = " + userId + " did not rate event whit id = " + eventId);
        }
    }

    @Override
    public List<RatingEventShortDto> findEventsWhitRating(StateAssessment assessment, Integer from, Integer size) {

        Sort sortPage = assessment == StateAssessment.LIKE
                ? Sort.by("likes").ascending()
                : Sort.by("dislikes").ascending();
        CustomPageRequest pageable = CustomPageRequest.by(from, size, sortPage);
        Page<RatingEventShortDto> page;

        if (assessment.equals(StateAssessment.LIKE)) {
            page = ratingEventRepository.findAllByLikes(pageable);
        } else {
            page = ratingEventRepository.findAllByDislikes(pageable);
        }
        Map<Long, Long> confirmedReqList = confirmedRequests.findConfirmedRequestsByEventIds(page.getContent()
                .stream().map(RatingEventShortDto::getEvent).map(EventShortDto::getId).collect(Collectors.toList()));

        if (confirmedReqList != null) {
            for (RatingEventShortDto ratingEventShortDto : page) {
                ratingEventShortDto.getEvent().setConfirmedRequests(
                        confirmedReqList.get(ratingEventShortDto.getEvent().getId()));
            }
        }
        return page.getContent();
    }
}
