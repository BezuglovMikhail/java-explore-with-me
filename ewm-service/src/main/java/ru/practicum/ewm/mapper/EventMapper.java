package ru.practicum.ewm.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.newdto.NewEventDto;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.request.UpdateEventAdminRequest;
import ru.practicum.ewm.request.UpdateEventUserRequest;
import ru.practicum.ewm.until.status.StateEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@UtilityClass
public class EventMapper {

    public Event toEvent(NewEventDto newEventDto, Category category, User initiator) {
        Event newEvent = new Event();
        newEvent.setAnnotation(newEventDto.getAnnotation());
        newEvent.setCategory(category);
        newEvent.setConfirmedRequests(0L);
        newEvent.setCreatedOn(LocalDateTime.now());
        newEvent.setDescription(newEventDto.getDescription());
        newEvent.setEventDate(newEventDto.getEventDate());
        newEvent.setInitiator(initiator);
        newEvent.setLocation(LocationMapper.toLocation(newEventDto.getLocation()));
        newEvent.setPaid(newEventDto.isPaid());
        newEvent.setParticipantLimit(newEventDto.getParticipantLimit());
        newEvent.setRequestModeration(newEventDto.isRequestModeration());
        newEvent.setState(StateEvent.PENDING);
        newEvent.setTitle(newEventDto.getTitle());
        return newEvent;
    }

    public Event toUpdateEvent(UpdateEventAdminRequest updateEventAdminRequest, Event oldEvent, Category category) {
        Event updateEvent = new Event();
        updateEvent.setId(oldEvent.getId());
        updateEvent.setAnnotation(updateEventAdminRequest.getAnnotation() != null
                ? updateEventAdminRequest.getAnnotation()
                : oldEvent.getAnnotation());
        updateEvent.setCategory(category != null
                ? category
                : oldEvent.getCategory());
        updateEvent.setCreatedOn(oldEvent.getCreatedOn());
        updateEvent.setDescription(updateEventAdminRequest.getDescription() != null
                ? updateEventAdminRequest.getDescription()
                : oldEvent.getDescription());
        updateEvent.setEventDate(oldEvent.getEventDate());
        updateEvent.setInitiator(oldEvent.getInitiator());
        updateEvent.setLocation(updateEventAdminRequest.getLocation() != null
                ? LocationMapper.toLocation(updateEventAdminRequest.getLocation())
                : oldEvent.getLocation());
        updateEvent.setPaid(updateEventAdminRequest.getPaid() != null
                ? updateEventAdminRequest.getPaid()
                : oldEvent.getPaid());
        updateEvent.setParticipantLimit(updateEventAdminRequest.getParticipantLimit() != null
                ? updateEventAdminRequest.getParticipantLimit()
                : oldEvent.getParticipantLimit());
        updateEvent.setPublishedOn(oldEvent.getPublishedOn());
        updateEvent.setRequestModeration(updateEventAdminRequest.getRequestModeration() != null
                ? updateEventAdminRequest.getRequestModeration()
                : oldEvent.getRequestModeration());
        updateEvent.setState(oldEvent.getState());
        updateEvent.setTitle(updateEventAdminRequest.getTitle() != null
                ? updateEventAdminRequest.getTitle()
                : oldEvent.getTitle());
        updateEvent.setViews(oldEvent.getViews());
        return updateEvent;
    }

    public Event toUpdateEvent(UpdateEventUserRequest updateEventUserRequest, Event oldEvent, Category category) {
        Event updateEvent = new Event();
        updateEvent.setId(oldEvent.getId());
        updateEvent.setAnnotation(updateEventUserRequest.getAnnotation() != null
                ? updateEventUserRequest.getAnnotation()
                : oldEvent.getAnnotation());
        updateEvent.setCategory(category != null
                ? category
                : oldEvent.getCategory());
        updateEvent.setCreatedOn(oldEvent.getCreatedOn());
        updateEvent.setDescription(updateEventUserRequest.getDescription() != null
                ? updateEventUserRequest.getDescription()
                : oldEvent.getDescription());
        updateEvent.setEventDate(oldEvent.getEventDate());
        updateEvent.setInitiator(oldEvent.getInitiator());
        updateEvent.setLocation(updateEventUserRequest.getLocation() != null
                ? LocationMapper.toLocation(updateEventUserRequest.getLocation())
                : oldEvent.getLocation());
        updateEvent.setPaid(updateEventUserRequest.getPaid() != null
                ? updateEventUserRequest.getPaid()
                : oldEvent.getPaid());
        updateEvent.setParticipantLimit(updateEventUserRequest.getParticipantLimit() != null
                ? updateEventUserRequest.getParticipantLimit()
                : oldEvent.getParticipantLimit());
        updateEvent.setPublishedOn(oldEvent.getPublishedOn());
        updateEvent.setRequestModeration(updateEventUserRequest.getRequestModeration() != null
                ? updateEventUserRequest.getRequestModeration()
                : oldEvent.getRequestModeration());
        updateEvent.setState(oldEvent.getState());
        updateEvent.setTitle(updateEventUserRequest.getTitle() != null
                ? updateEventUserRequest.getTitle()
                : oldEvent.getTitle());
        updateEvent.setViews(oldEvent.getViews());
        return updateEvent;
    }

    public EventFullDto toEventFullDto(Event event, Long confirmedRequests) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventFullDto.setConfirmedRequests(confirmedRequests != null
                ? confirmedRequests
                : 0L);
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventFullDto.setLocation(LocationMapper.toLocationDto(event.getLocation()));
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setViews(event.getViews());
        return eventFullDto;
    }

    public EventFullDto toEventFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventFullDto.setLocation(LocationMapper.toLocationDto(event.getLocation()));
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setViews(event.getViews());
        return eventFullDto;
    }

    public EventShortDto toEventShortDto(Event event, Long confirmedRequests) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventShortDto.setConfirmedRequests(confirmedRequests);
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setViews(event.getViews());
        return eventShortDto;
    }

    public EventShortDto toEventShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setViews(event.getViews());
        return eventShortDto;
    }

    public List<EventShortDto> mapToEventShortDto(Iterable<Event> events, Map<Long, Long> eventsConfirmedRequest) {
        List<EventShortDto> result = new ArrayList<>();

        for (Event event : events) {
            result.add(toEventShortDto(event, eventsConfirmedRequest.get(event.getId())));
        }
        return result;
    }


    public List<EventShortDto> mapToEventShortDto(Iterable<Event> events) {
        List<EventShortDto> result = new ArrayList<>();

        for (Event event : events) {
            result.add(toEventShortDto(event));
        }
        return result;
    }

    public List<EventFullDto> mapToEventFullDto(Iterable<Event> events, Map<Long, Long> eventsConfirmedRequest) {
        List<EventFullDto> result = new ArrayList<>();

        for (Event event : events) {
            result.add(toEventFullDto(event, eventsConfirmedRequest.get(event.getId())));
        }
        return result;
    }
}
