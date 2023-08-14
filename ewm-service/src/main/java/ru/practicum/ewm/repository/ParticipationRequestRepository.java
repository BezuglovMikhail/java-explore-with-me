package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.dto.CountRequestDto;
import ru.practicum.ewm.model.ParticipationRequest;
import ru.practicum.ewm.until.status.State;
import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByRequester_id(long requesterId);

    List<ParticipationRequest> findAllByEvent_Id(Long eventId);

    List<ParticipationRequest> findByIdIn(List<Long> requestIds);

    ParticipationRequest findByRequester_IdAndEvent_Id(Long requesterId, Long eventId);

    Long countByEventIdAndState(Long eventId, State confirmed);

    @Query("SELECT new ru.practicum.ewm.dto.CountRequestDto(event.id, COUNT(event.id)) " +
            "FROM ParticipationRequest as pr " +
            "WHERE event.id IN (?1) AND pr.state = ?2 " +
            "GROUP BY event.id")
    List<CountRequestDto> countByEventInAndState(List<Long> eventIds, State confirmed);
}
