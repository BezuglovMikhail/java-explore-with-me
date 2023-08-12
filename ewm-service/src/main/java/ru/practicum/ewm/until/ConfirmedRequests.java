package ru.practicum.ewm.until;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.repository.ParticipationRequestRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.ewm.until.status.State.CONFIRMED;

@Component
public class ConfirmedRequests {

    @Autowired
    private ParticipationRequestRepository participationRequestRepository;

    public HashMap<Long, Integer> findConfirmedRequests(List<Event> events) {

        List<Long> eventsId = events.stream().map(Event::getId).collect(Collectors.toList());

        HashMap<Long, Integer> confirmedRequests = new HashMap<>();

        for (Long eventId : eventsId) {
            confirmedRequests.put(eventId, participationRequestRepository
                    .countByEventIdAndState(eventId, CONFIRMED));
        }

        return confirmedRequests;
    }

    public HashMap<Long, Integer> findConfirmedRequests(Set<Event> events) {

        List<Long> eventsId = events.stream().map(Event::getId).collect(Collectors.toList());

        HashMap<Long, Integer> confirmedRequests = new HashMap<>();

        for (Long eventId : eventsId) {
            confirmedRequests.put(eventId, participationRequestRepository
                    .countByEventIdAndState(eventId, CONFIRMED));
        }

        return confirmedRequests;
    }
}
