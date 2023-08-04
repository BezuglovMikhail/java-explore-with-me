package ru.practicum.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.model.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByRequester_id(long requesterId);

    List<ParticipationRequest> findAllByRequester_idAndEvent_id(long requesterId, long eventId);

    /*@Query("SELECT pr FROM ParticipationRequest as pr " +
            "WHERE pr.id IN (?1)")
    List<ParticipationRequest> findEventRequests(List<Long> requestIds);*/

    List<ParticipationRequest> findByIdIn(List<Long> requestIds);

    List<ParticipationRequest> findAllByRequester_idAndEvent_idAndStatus(long requesterId, long eventId, String state);

}
