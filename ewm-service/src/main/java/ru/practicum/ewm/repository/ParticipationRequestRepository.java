package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.ParticipationRequest;
import ru.practicum.ewm.status.State;

import java.util.HashMap;
import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByRequester_id(long requesterId);

    List<ParticipationRequest> findAllByEvent_Id(Long eventId);

    List<ParticipationRequest> findByIdIn(List<Long> requestIds);

    ParticipationRequest findByRequester_IdAndEvent_Id(Long requesterId, Long eventId);

    /* @Query("select it from Item it " +
            "where lower(it.name) like lower(concat('%', :search, '%')) " +
            " or lower(it.description) like lower(concat('%', :search, '%')) " +
            " and it.available = true")*/
    Integer countByEventIdAndState(Long eventId, State confirmed);

    HashMap<Long, Integer> countByEventIdInAndState(List<Long> events, State confirmed);
}
