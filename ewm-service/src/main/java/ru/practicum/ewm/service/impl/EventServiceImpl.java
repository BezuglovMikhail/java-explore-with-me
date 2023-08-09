package ru.practicum.ewm.service.impl;

import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import ru.practicum.ewm.CustomPageRequest;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.NewEventDto;
import ru.practicum.ewm.exeption.IncorrectParameterException;
import ru.practicum.ewm.exeption.NotFoundException;
import ru.practicum.ewm.exeption.ValidationException;
import ru.practicum.ewm.model.*;
import ru.practicum.ewm.model.QEvent;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.LocationRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.request.UpdateEventAdminRequest;
import ru.practicum.ewm.request.UpdateEventUserRequest;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.status.EventSort;
import ru.practicum.ewm.status.State;
import ru.practicum.stats.client.StatClient;
import ru.practicum.stats.dto.ViewStatDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.ewm.mapper.EventMapper.*;

@Service
@Slf4j
public class EventServiceImpl implements EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private final StatClient statsClient;

    private final String format = ("yyyy-MM-dd HH:mm:ss");

    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository, CategoryRepository categoryRepository, LocationRepository locationRepository, StatClient statsClient) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.locationRepository = locationRepository;
        this.statsClient = statsClient;
    }

    @Transactional
    @Override
    public EventFullDto save(NewEventDto newEventDto, Long userId) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User whit id = " + userId + " not found in database."));
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category whit id = " + newEventDto.getCategory() + " not found in database."));

        if (newEventDto.getEventDate().isAfter(LocalDateTime.now().plusHours(2L))
                || newEventDto.getEventDate().isAfter(LocalDateTime.now())) {
            Location newLocation = locationRepository.save(newEventDto.getLocation());

            try {
                return toEventFullDto(eventRepository.save(toEvent(newEventDto, category, initiator, newLocation)));
            } catch (DataIntegrityViolationException | ConstraintViolationException e) {
                throw new ValidationException(e.getClass().getName(), e.getMessage(), e.getLocalizedMessage(),
                        HttpStatus.BAD_REQUEST, LocalDateTime.now());
            }
        } else {
            throw new ValidationException(getClass().getName(), "Integrity constraint has been violated.",
                    "could not execute statement; SQL [n/a]; constraint [uq_category_name];" +
                            " nested exception is org.hibernate.exception.ConstraintViolationException:" +
                            " could not execute statement",
                    HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
    }

    @Override
    public EventFullDto getEventByIdForUserId(Long userId, Long eventId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User whit id = " + userId + " not found in database."));
        Event eventByUserIdAndEventId = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event whit id = " + eventId + " not found in database."));

        setViews(List.of(eventByUserIdAndEventId));
        return toEventFullDto(eventByUserIdAndEventId);
    }

    @Override
    public EventFullDto getEventById(Long eventId) {
        Event findEvent = eventRepository.findByIdAndState(eventId, State.PUBLISHED);
        if (Objects.isNull(findEvent)) {
            throw new NotFoundException("Event whit id = " + eventId + " not found in database.");
        }
        setViews(List.of(findEvent));
        return toEventFullDto(findEvent);
    }

    @Override
    @Transactional
    public EventFullDto updateEventAdmin(UpdateEventAdminRequest updateEventAdminRequest, long eventId) {
        Event oldEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event whit id = " + eventId + " not found in database."));
        Category category = new Category();
        checkEventDayForUpdate(updateEventAdminRequest.getEventDate(), oldEvent.getEventDate());

        if (updateEventAdminRequest.getCategory() != null) {
            category = categoryRepository.findById(updateEventAdminRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category whit id = " +
                            updateEventAdminRequest.getCategory() + " not found in database."));
        } else {
            category = oldEvent.getCategory();
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction() == State.PUBLISH_EVENT
                    && oldEvent.getState() == State.PENDING) {
                updateEventAdminRequest.setStateAction(State.PUBLISHED);
            } else if (updateEventAdminRequest.getStateAction() == State.REJECT_EVENT
                    && oldEvent.getState() == State.PENDING) {
                updateEventAdminRequest.setStateAction(State.CANCELED);
            } else {
                throw new IncorrectParameterException(getClass().getName(), "Integrity constraint has been violated.",
                        "could not execute statement; SQL [n/a]; constraint [uq_category_name];" +
                                " nested exception is org.hibernate.exception.ConstraintViolationException:" +
                                " could not execute statement",
                        HttpStatus.CONFLICT, LocalDateTime.now());
            }
        }

        return toEventFullDto(eventRepository.save(toUpdateEvent(updateEventAdminRequest, oldEvent, category)));
    }

    @Override
    @Transactional
    public EventFullDto updateEventPrivate(UpdateEventUserRequest updateEventUserRequest, long eventId) {
        Event oldEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event whit id = " + eventId + " not found in database."));
        Category category;
        checkStateForUpdatePrivate(oldEvent.getState());
        checkEventDayForUpdate(updateEventUserRequest.getEventDate(), oldEvent.getEventDate());

        if (updateEventUserRequest.getCategory() != null) {
            category = categoryRepository.findById(updateEventUserRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category whit id = " +
                            updateEventUserRequest.getCategory() + " not found in database."));
        } else {
            category = oldEvent.getCategory();
        }
        if (updateEventUserRequest.getStateAction() != null
                && updateEventUserRequest.getStateAction() != State.CANCEL_REVIEW) {
            updateEventUserRequest.setStateAction(State.PENDING);
        } else {
            updateEventUserRequest.setStateAction(State.CANCELED);
        }
        return toEventFullDto(eventRepository.save(toUpdateEvent(updateEventUserRequest, oldEvent, category)));
    }

    @Transactional
    @Override
    public void deleteEvent(long eventId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event whit id = " + eventId + " not found in database."));
        eventRepository.deleteById(eventId);
    }

    @Override
    public List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size) {
        Sort sort = Sort.by("id").ascending();
        CustomPageRequest pageable = CustomPageRequest.by(from, size, sort);
        Page<Event> page = eventRepository.findAllByInitiator_Id(userId, pageable);
        setViews(page.getContent());
        return mapToEventShortDto(page.getContent());
    }

    @Override
    public List<EventFullDto> adminFindEventsWhitFilter(List<Long> users, List<State> states, List<Long> categories,
                                                        String rangeStart, String rangeEnd,
                                                        Integer from, Integer size) {
        Sort sort = Sort.by("id").ascending();
        CustomPageRequest pageable = CustomPageRequest.by(from, size, sort);

        LocalDateTime start = toLocalDateTime(rangeStart);
        LocalDateTime end = toLocalDateTime(rangeEnd);
        checkStartEndSearch(start, end);
        SearchFilter filter = new SearchFilter(
                users,
                states,
                categories,
                start,
                end,
                null,
                null,
                null
        );

        BooleanBuilder booleanBuilder = makeBooleanBuilder(filter);
        Page<Event> page = eventRepository.findAll(booleanBuilder, pageable);
        return mapToEventFullDto(page);
    }

    @Override
    public List<EventShortDto> publicFindEventsWhitFilter(String text, Boolean paid, Boolean onlyAvailable,
                                                          EventSort sort, List<Long> users,
                                                          List<Long> categories, String rangeStart,
                                                          String rangeEnd, Integer from, Integer size) {
        Sort sortPage = sort == EventSort.EVENT_DATE
                ? Sort.by("eventDate").ascending()
                : Sort.by("views").ascending();
        CustomPageRequest pageable = CustomPageRequest.by(from, size, sortPage);

        LocalDateTime start = toLocalDateTime(rangeStart);
        LocalDateTime end = toLocalDateTime(rangeEnd);
        checkStartEndSearch(start, end);

        SearchFilter filter = new SearchFilter(
                users,
                List.of(State.PUBLISHED),
                categories,
                start,
                end,
                text,
                paid,
                onlyAvailable
        );

        BooleanBuilder booleanBuilder = makeBooleanBuilder(filter);
        Page<Event> page = eventRepository.findAll(booleanBuilder, pageable);
        setViews(page.getContent());
        return mapToEventShortDto(page);
    }

    private BooleanBuilder makeBooleanBuilder(SearchFilter filter) {
        java.util.function.Predicate<Object> isNullOrEmpty = obj ->
                Objects.isNull(obj) || (obj instanceof Collection && ((Collection<?>) obj).isEmpty());
        QEvent qEvent = QEvent.event;
        return new BooleanBuilder()
                .and(!isNullOrEmpty.test(filter.getUsers()) ? qEvent.initiator.id.in(filter.getUsers()) : null)
                .and(!isNullOrEmpty.test(filter.getStates()) ? qEvent.state.in(filter.getStates()) : null)
                .and(!isNullOrEmpty.test(filter.getCategories()) ? qEvent.category.id.in(filter.getCategories()) : null)
                .and(!isNullOrEmpty.test(filter.getRangeStart()) ? qEvent.eventDate.after(filter.getRangeStart()) : null)
                .and(!isNullOrEmpty.test(filter.getPaid()) ? qEvent.paid.eq(filter.getPaid()) : null)
                .and(!isNullOrEmpty.test(filter.getText())
                        ? (qEvent.annotation.likeIgnoreCase(filter.getText()).or(qEvent.description.likeIgnoreCase(filter.getText()))) : null)
                .and(!isNullOrEmpty.test(filter.getOnlyAvailable())
                        ? qEvent.participantLimit.eq(0).or(qEvent.confirmedRequests.lt(qEvent.participantLimit)) : null);
    }

    private LocalDateTime toLocalDateTime(String value) {
        return value != null ? LocalDateTime.parse(value, DateTimeFormatter.ofPattern(format)) : null;
    }

    private void checkStartEndSearch(LocalDateTime start, LocalDateTime end) {
        if (Objects.isNull(start) || Objects.isNull(end)) {
            return;
        }
        if (end.isBefore(start)) {
            throw new ValidationException(getClass().getName(), "Integrity constraint has been violated.",
                    "could not execute statement; SQL [n/a]; constraint [uq_category_name];" +
                            " nested exception is org.hibernate.exception.ConstraintViolationException:" +
                            " could not execute statement",
                    HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
    }

    private void checkEventDayForUpdate(LocalDateTime eventDay,
                                        LocalDateTime oldEventDay) throws IncorrectParameterException {
        if (oldEventDay.isAfter(LocalDateTime.now().plusHours(2L))) {
            if (eventDay != null
                    && (eventDay.isBefore(LocalDateTime.now().plusHours(1L))
                    || eventDay.isBefore(LocalDateTime.now()))) {

                throw new ValidationException(getClass().getName(), "Integrity constraint has been violated.",
                        "could not execute statement; SQL [n/a]; constraint [uq_category_name];" +
                                " nested exception is org.hibernate.exception.ConstraintViolationException:" +
                                " could not execute statement",
                        HttpStatus.BAD_REQUEST, LocalDateTime.now());
            }
        }
    }

    private void checkStateForUpdatePrivate(State state) throws IncorrectParameterException {
        if (state.equals(State.PUBLISHED)) {
            throw new IncorrectParameterException(getClass().getName(), "Integrity constraint has been violated.",
                    "could not execute statement; SQL [n/a]; constraint [uq_category_name];" +
                            " nested exception is org.hibernate.exception.ConstraintViolationException:" +
                            " could not execute statement",
                    HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
    }

    private void setViews(List<Event> events) {
        setViews(events, LocalDateTime.of(0, 1, 1, 0, 0, 0),
                LocalDateTime.now());
    }

    private void setViews(List<Event> events, LocalDateTime start, LocalDateTime end) {

        if (Objects.isNull(events) || events.isEmpty()) {
            return;
        }
        List<String> uris = events.stream()
                .map(event -> "/events/" + event.getId())
                .collect(Collectors.toList());

        List<ViewStatDto> dtoList = new ArrayList<>();
        try {
            List<ViewStatDto> responseEntity = statsClient.getStatistics(start, end, uris, true);
            if (responseEntity != null) {
                dtoList = responseEntity;
            }
        } catch (RestClientException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return;
        }
        List<ViewStatDto> finalDtoList = dtoList;
        events.forEach(event -> setViewsFromSources(event, finalDtoList != null ? finalDtoList : List.of()));
    }

    private void setViewsFromSources(Event event, List<ViewStatDto> sources) {
        (Objects.isNull(sources) ? new ArrayList<ViewStatDto>(0) : sources)
                .stream()
                .filter(Objects::nonNull)
                .filter(dto -> ("/events/" + event.getId()).equals(dto.getUri()))
                .findFirst()
                .ifPresentOrElse(
                        dto -> event.setViews(dto.getHits()),
                        () -> event.setViews(0L));
    }
}
