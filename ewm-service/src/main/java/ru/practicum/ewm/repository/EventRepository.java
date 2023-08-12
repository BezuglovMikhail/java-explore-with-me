package ru.practicum.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.until.status.State;

import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    Page<Event> findAllByInitiator_Id(Long userId, Pageable pageable);

    Set<Event> findByIdIn(Set<Long> events);

    Event findByIdAndState(Long eventId, State state);
}
