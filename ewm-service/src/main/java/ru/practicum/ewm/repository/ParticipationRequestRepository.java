package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByRequester_id(long requesterId);

    List<ParticipationRequest> findAllByEvent_Id(Long eventId);

    List<ParticipationRequest> findByIdIn(List<Long> requestIds);

    ParticipationRequest findByRequester_IdAndEvent_Id(Long requesterId, Long eventId);
}
