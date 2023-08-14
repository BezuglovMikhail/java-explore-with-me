package ru.practicum.ewm.until;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.CountRequestDto;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.repository.ParticipationRequestRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.ewm.until.status.State.CONFIRMED;

@Component
public class ConfirmedRequests {

    @Autowired
    private ParticipationRequestRepository participationRequestRepository;

    public Map<Long, Long> findConfirmedRequests(Collection<Event> events) {

        List<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toList());

        List<CountRequestDto> countRequests = participationRequestRepository.countByEventInAndState(eventIds, CONFIRMED);


        return countRequests.stream().collect(Collectors
                .toMap(CountRequestDto::getEventId, CountRequestDto::getCount));
    }

    public Long findCountRequests(Long eventId) {
        return participationRequestRepository.countByEventIdAndState(eventId, CONFIRMED);
    }
}