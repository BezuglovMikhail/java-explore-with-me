package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.ParticipationRequest;
import ru.practicum.ewm.status.State;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByRequester_id(long requesterId);

    //List<ParticipationRequest> findAllByRequester_idAndEvent_id(long requesterId, long eventId);

    List<ParticipationRequest> findAllByEvent_Id(Long eventId);

    /*@Query("SELECT pr FROM ParticipationRequest as pr " +
            "WHERE pr.id IN (?1)")
    List<ParticipationRequest> findEventRequests(List<Long> requestIds);*/

    List<ParticipationRequest> findByIdIn(List<Long> requestIds);

    ParticipationRequest findByRequester_IdAndEvent_Id(Long requesterId, Long eventId);


    List<ParticipationRequest> findAllByEvent_IdAndState(Long eventId, State state);
}
