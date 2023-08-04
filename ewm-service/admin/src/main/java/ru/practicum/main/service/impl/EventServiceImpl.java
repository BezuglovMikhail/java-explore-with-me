package ru.practicum.main.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.main.CustomPageRequest;
import ru.practicum.main.dto.EventFullDto;
import ru.practicum.main.dto.EventShortDto;
import ru.practicum.main.dto.NewEventDto;
import ru.practicum.main.exeption.NotFoundException;
import ru.practicum.main.model.*;
import ru.practicum.main.repository.CategoryRepository;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.LocationRepository;
import ru.practicum.main.repository.UserRepository;
import ru.practicum.main.request.UpdateEventAdminRequest;
import ru.practicum.main.service.EventService;
import ru.practicum.main.status.EventSort;
import ru.practicum.main.status.State;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.main.mapper.EventMapper.*;

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

    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository, CategoryRepository categoryRepository, LocationRepository locationRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public EventFullDto save(NewEventDto newEventDto, Long userId) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User whit id = " + userId + " not found in database."));
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category whit id = " + newEventDto.getCategory() + " not found in database."));
        Location newLocation = locationRepository.save(newEventDto.getLocation());

        Event newEvent = toEvent(newEventDto, category, initiator, newLocation);

        EventFullDto newEventFullDto = toEventFullDto(eventRepository.save(newEvent));

        return newEventFullDto;
    }

    @Override
    public EventFullDto getEventByIdForUserId(Long userId, Long eventId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User whit id = " + userId + " not found in database."));

        EventFullDto eventFullDtoById = toEventFullDto(eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event whit id = " + userId + " not found in database.")));
        return eventFullDtoById;
    }

    @Override
    public EventFullDto getEventById(Long id) {
        return toEventFullDto(eventRepository.findEventById(id));
    }

    @Override
    public EventFullDto updateEvent(UpdateEventAdminRequest updateEventAdminRequest, long eventId) {
        Event oldEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event whit id = " + eventId + " not found in database."));
        Category category = categoryRepository.findById(updateEventAdminRequest.getCategory())
                .orElseThrow(() -> new NotFoundException("Category whit id = " +
                        updateEventAdminRequest.getCategory() + " not found in database."));

        EventFullDto updateEvent = toEventFullDto(eventRepository
                .save(toUpdateEvent(updateEventAdminRequest, oldEvent, category)));

        return updateEvent;
    }

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

        return mapToEventShortDto(page.getContent());
    }

    @Override
    public List<EventFullDto> adminFindEventsWhitFilter(List<Long> users, List<State> states, List<Long> categories,
                                                   String rangeStart, String rangeEnd,
                                                   Integer from, Integer size) {
        Sort sort = Sort.by("id").ascending();
        CustomPageRequest pageable = CustomPageRequest.by(from, size, sort);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime rangeStartFormatter = LocalDateTime.parse(rangeStart, formatter);
        LocalDateTime rangeEndFormatter = LocalDateTime.parse(rangeEnd, formatter);

        Specification<Event> specification = SearchFilter
                .builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStartFormatter)
                .rangeEnd(rangeEndFormatter)
                .build()
                .makeSpecification();
        Page<Event> page = eventRepository.findAll(specification, pageable);
        return mapToEventFullDto(page);
    }

    @Override
    public List<EventFullDto> publicFindEventsWhitFilter(String text, Boolean paid, Boolean onlyAvailable,
                                                         EventSort sort, List<Long> users, List<State> states,
                                                         List<Long> categories, String rangeStart,
                                                         String rangeEnd, Integer from, Integer size) {
        Sort sortPage = sort == EventSort.EVENT_DATE
                ? Sort.by("eventDate").ascending()
                : Sort.by("views").ascending();
        CustomPageRequest pageable = CustomPageRequest.by(from, size, sortPage);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime rangeStartFormatter = LocalDateTime.parse(rangeStart, formatter);
        LocalDateTime rangeEndFormatter = LocalDateTime.parse(rangeEnd, formatter);

        Specification<Event> specification = SearchFilter
                .builder()
                .text(text)
                .paid(paid)
                .onlyAvailable(onlyAvailable)
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStartFormatter)
                .rangeEnd(rangeEndFormatter)
                .build()
                .makeSpecification();
        Page<Event> page = eventRepository.findAll(specification, pageable);
        return mapToEventFullDto(page);
    }
}

