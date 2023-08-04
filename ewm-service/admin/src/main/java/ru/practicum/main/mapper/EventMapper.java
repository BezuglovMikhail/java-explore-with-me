package ru.practicum.main.mapper;

import ru.practicum.main.dto.EventFullDto;
import ru.practicum.main.dto.EventShortDto;
import ru.practicum.main.dto.NewEventDto;
import ru.practicum.main.model.Category;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.Location;
import ru.practicum.main.model.User;
import ru.practicum.main.request.UpdateEventAdminRequest;
import ru.practicum.main.status.State;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.main.mapper.CategoryMapper.toCategoryDto;
import static ru.practicum.main.mapper.UserMapper.toUserShortDto;

public class EventMapper {

    private EventMapper() {
    }

    public static Event toEvent(NewEventDto newEventDto, Category category, User initiator, Location newLocation) {
        Event newEvent = new Event();
        newEvent.setId(newEventDto.getId());
        newEvent.setAnnotation(newEventDto.getAnnotation());
        newEvent.setCategory(category);
        newEvent.setConfirmedRequests(0);
        newEvent.setCreatedOn(LocalDateTime.now());
        newEvent.setDescription(newEventDto.getDescription());
        newEvent.setEventDate(newEventDto.getEventDate());
        newEvent.setInitiator(initiator);
        newEvent.setLocation(newLocation);
        newEvent.setPaid(newEventDto.isPaid());
        newEvent.setParticipantLimit(newEventDto.getParticipantLimit());
        newEvent.setPublishedOn(newEventDto.getPublishedOn());
        newEvent.setRequestModeration(newEventDto.isRequestModeration());
        newEvent.setState(State.PENDING);
        newEvent.setTitle(newEventDto.getTitle());
        newEvent.setViews(0);
        return newEvent;
    }

    public static Event toUpdateEvent(UpdateEventAdminRequest updateEventAdminRequest, Event oldEvent, Category category) {
        Event updateEvent = new Event();
        updateEvent.setId(oldEvent.getId());
        updateEvent.setAnnotation(updateEventAdminRequest.getAnnotation());
        updateEvent.setCategory(category);
        updateEvent.setConfirmedRequests(oldEvent.getConfirmedRequests());
        updateEvent.setCreatedOn(oldEvent.getCreatedOn());
        updateEvent.setDescription(updateEventAdminRequest.getDescription());
        updateEvent.setEventDate(oldEvent.getEventDate());
        updateEvent.setInitiator(oldEvent.getInitiator());
        updateEvent.setLocation(updateEventAdminRequest.getLocation());
        updateEvent.setPaid(updateEventAdminRequest.isPaid());
        updateEvent.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        updateEvent.setPublishedOn(oldEvent.getPublishedOn());
        updateEvent.setRequestModeration(updateEvent.isRequestModeration());
        updateEvent.setState(updateEventAdminRequest.getStateAction());
        updateEvent.setTitle(updateEventAdminRequest.getTitle());
        updateEvent.setViews(oldEvent.getViews());
        return updateEvent;
    }

    public static EventFullDto toEventFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(toCategoryDto(event.getCategory()));
        eventFullDto.setConfirmedRequests(event.getConfirmedRequests());
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setInitiator(toUserShortDto(event.getInitiator()));
        eventFullDto.setLocation(event.getLocation());
        eventFullDto.setPaid(event.isPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setRequestModeration(event.isRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setViews(event.getViews());
        return eventFullDto;
    }

    public static EventShortDto toEventShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(toCategoryDto(event.getCategory()));
        eventShortDto.setConfirmedRequests(event.getConfirmedRequests());
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setInitiator(toUserShortDto(event.getInitiator()));
        eventShortDto.setPaid(event.isPaid());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setViews(event.getViews());
        return eventShortDto;
    }

    public static List<EventShortDto> mapToEventShortDto(Iterable<Event> events) {
        List<EventShortDto> result = new ArrayList<>();

        for (Event event : events) {
            result.add(toEventShortDto(event));
        }

        return result;
    }

    public static List<EventFullDto> mapToEventFullDto(Iterable<Event> events) {
        List<EventFullDto> result = new ArrayList<>();

        for (Event event : events) {
            result.add(toEventFullDto(event));
        }

        return result;
    }

}
