package ru.practicum.ewm.service.impl;

import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.NewEventDto;
import ru.practicum.ewm.exeption.IncorrectParameterException;
import ru.practicum.ewm.exeption.NotFoundException;
import ru.practicum.ewm.exeption.ValidationException;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.QEvent;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.request.UpdateEventAdminRequest;
import ru.practicum.ewm.request.UpdateEventUserRequest;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.until.ConfirmedRequests;
import ru.practicum.ewm.until.CustomPageRequest;
import ru.practicum.ewm.until.SearchFilter;
import ru.practicum.ewm.until.status.*;
import ru.practicum.stats.client.StatClient;
import ru.practicum.stats.dto.ViewStatDto;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static ru.practicum.ewm.mapper.EventMapper.*;
import static ru.practicum.ewm.until.status.EventSort.VIEWS;

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
    private final StatClient statsClient;
    @Autowired
    private final ConfirmedRequests confirmedRequests;

    private final String format = ("yyyy-MM-dd HH:mm:ss");

    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository,
                            CategoryRepository categoryRepository,
                            StatClient statsClient, ConfirmedRequests confirmedRequests) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.statsClient = statsClient;
        this.confirmedRequests = confirmedRequests;
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
            try {
                return toEventFullDto(eventRepository.save(toEvent(newEventDto, category, initiator)));
            } catch (DataIntegrityViolationException | ConstraintViolationException e) {
                throw new ValidationException("Validation exception");
            }
        } else {
            throw new ValidationException("Validation exception");
        }
    }

    @Override
    public EventFullDto getEventByIdForUserId(Long userId, Long eventId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User whit id = " + userId + " not found in database."));
        Event eventByUserIdAndEventId = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event whit id = " + eventId + " not found in database."));
        Long confirmedReq = confirmedRequests.findCountRequests(eventByUserIdAndEventId.getId());
        setViews(List.of(eventByUserIdAndEventId));
        return toEventFullDto(eventByUserIdAndEventId, confirmedReq);
    }

    @Override
    public EventFullDto getEventById(Long eventId) {
        Event findEvent = eventRepository.findByIdAndState(eventId, StateEvent.PUBLISHED);
        if (Objects.isNull(findEvent)) {
            throw new NotFoundException("Event whit id = " + eventId + " not found in database.");
        }
        Long confirmedReq = confirmedRequests.findCountRequests(findEvent.getId());
        setViews(List.of(findEvent));
        return toEventFullDto(findEvent, confirmedReq);
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
            if (updateEventAdminRequest.getStateAction().equals(StateAction.PUBLISH_EVENT)
                    && oldEvent.getState().equals(StateEvent.PENDING)) {
                oldEvent.setState(StateEvent.PUBLISHED);
                oldEvent.setPublishedOn(LocalDateTime.now());
            } else if (updateEventAdminRequest.getStateAction().equals(StateAction.REJECT_EVENT)
                    && oldEvent.getState().equals(StateEvent.PENDING)) {
                oldEvent.setState(StateEvent.CANCELED);
            } else {
                throw new IncorrectParameterException("Incorrect parameter");
            }
        }
        Event updateEvent = eventRepository.save(toUpdateEvent(updateEventAdminRequest, oldEvent, category));
        return toEventFullDto(updateEvent);
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
                && !updateEventUserRequest.getStateAction().equals(StateActionReview.CANCEL_REVIEW)) {
            oldEvent.setState(StateEvent.PENDING);
        } else {
            oldEvent.setState(StateEvent.CANCELED);
        }

        Event updateEvent = eventRepository.save(toUpdateEvent(updateEventUserRequest, oldEvent, category));
        return toEventFullDto(updateEvent);
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

        return mapToEventShortDto(page.getContent(), confirmedRequests.findConfirmedRequests(page.getContent()));
    }

    @Override
    public List<EventFullDto> adminFindEventsWhitFilter(List<Long> users, List<StateEvent> states, List<Long> categories,
                                                        LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                        Integer from, Integer size) {
        Sort sort = Sort.by("id").ascending();
        CustomPageRequest pageable = CustomPageRequest.by(from, size, sort);

        checkStartEndSearch(rangeStart, rangeEnd);
        SearchFilter filter = new SearchFilter(
                users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                null,
                null,
                null
        );

        BooleanBuilder booleanBuilder = makeBooleanBuilder(filter);
        Page<Event> page = eventRepository.findAll(booleanBuilder, pageable);
        Map<Long, Long> countRequest = confirmedRequests.findConfirmedRequests(page.getContent());

        List<EventFullDto> findEvents = mapToEventFullDto(page.getContent(), countRequest);

        return findEvents;
    }

    @Override
    public List<EventShortDto> publicFindEventsWhitFilter(String text, Boolean paid, Boolean onlyAvailable,
                                                          EventSort sort, List<Long> users,
                                                          List<Long> categories, LocalDateTime rangeStart,
                                                          LocalDateTime rangeEnd, Integer from, Integer size) {
        Sort sortPage = sort == EventSort.EVENT_DATE
                ? Sort.by("eventDate").ascending()
                : Sort.by("id").ascending();
        CustomPageRequest pageable = CustomPageRequest.by(from, size, sortPage);

        checkStartEndSearch(rangeStart, rangeEnd);

        SearchFilter filter = new SearchFilter(
                users,
                List.of(StateEvent.PUBLISHED),
                categories,
                rangeStart,
                rangeEnd,
                text,
                paid,
                onlyAvailable
        );

        BooleanBuilder booleanBuilder = makeBooleanBuilder(filter);
        Page<Event> page = eventRepository.findAll(booleanBuilder, pageable);
        if (Objects.isNull(rangeStart) || Objects.isNull(rangeEnd)) {
            setViews(page.getContent());
        } else {
            setViews(page.getContent(), rangeStart, rangeEnd);
        }
        setViews(page.getContent());
        List<EventShortDto> publicFindEvents = mapToEventShortDto(page,
                confirmedRequests.findConfirmedRequests(page.getContent()));

        if (!Objects.isNull(sort) && sort.equals(VIEWS)) {
            return publicFindEvents.stream().sorted(Comparator.comparing(EventShortDto::getViews))
                    .collect(toList());
        }
        return publicFindEvents;
    }

    private BooleanBuilder makeBooleanBuilder(SearchFilter filter) {
        java.util.function.Predicate<Object> isNullOrEmpty = obj ->
                Objects.isNull(obj) || (obj instanceof Collection && ((Collection<?>) obj).isEmpty());
        QEvent qEvent = QEvent.event;
        return new BooleanBuilder()
                .and(!isNullOrEmpty.test(filter.getUsers())
                        ? qEvent.initiator.id.in(filter.getUsers()) : null)
                .and(!isNullOrEmpty.test(filter.getStates())
                        ? qEvent.state.in(filter.getStates()) : null)
                .and(!isNullOrEmpty.test(filter.getCategories())
                        ? qEvent.category.id.in(filter.getCategories()) : null)
                .and(!isNullOrEmpty.test(filter.getRangeStart())
                        ? qEvent.eventDate.after(filter.getRangeStart()) : null)
                .and(!isNullOrEmpty.test(filter.getPaid())
                        ? qEvent.paid.eq(filter.getPaid()) : null)
                .and(!isNullOrEmpty.test(filter.getText())
                        ? (qEvent.annotation.likeIgnoreCase(filter.getText())
                        .or(qEvent.description.likeIgnoreCase(filter.getText()))) : null);
    }

    private void checkStartEndSearch(LocalDateTime start, LocalDateTime end) {
        if (Objects.isNull(start) || Objects.isNull(end)) {
            return;
        }
        if (end.isBefore(start)) {
            throw new ValidationException("Validation exception");
        }
    }

    private void checkEventDayForUpdate(LocalDateTime eventDay,
                                        LocalDateTime oldEventDay) throws IncorrectParameterException {
        if (oldEventDay.isAfter(LocalDateTime.now().plusHours(2L))) {
            if (eventDay != null
                    && (eventDay.isBefore(LocalDateTime.now().plusHours(1L))
                    || eventDay.isBefore(LocalDateTime.now()))) {

                throw new ValidationException("Validation exception");
            }
        }
    }

    private void checkStateForUpdatePrivate(StateEvent state) throws IncorrectParameterException {
        if (state.equals(StateEvent.PUBLISHED)) {
            throw new IncorrectParameterException("Incorrect parameter");
        }
    }

    private void setViews(List<Event> events) {
        LocalDateTime start = LocalDateTime.MIN;
        if (!Objects.isNull(events)) {
            for (Event event : events) {
                if (!Objects.isNull(event.getPublishedOn()) && event.getPublishedOn().isAfter(start)) {
                    start = event.getPublishedOn();
                }
            }
        }
        if (start != LocalDateTime.MIN) {
            setViews(events, start, LocalDateTime.now());
        }
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
