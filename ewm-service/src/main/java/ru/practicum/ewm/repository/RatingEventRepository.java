package ru.practicum.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.dto.RatingDto;
import ru.practicum.ewm.dto.RatingEventShortDto;
import ru.practicum.ewm.model.RatingEvent;
import ru.practicum.ewm.until.status.StateAssessment;

public interface RatingEventRepository extends JpaRepository<RatingEvent, Long> {

    @Query("SELECT new ru.practicum.ewm.dto.RatingDto(event.id, " +
            "SUM(CASE re.state WHEN 'LIKE' THEN 1 ELSE 0 END) as likes, " +
            "SUM(CASE re.state WHEN 'DISLIKE' THEN 1 ELSE 0 END) as dislikes) " +
            "FROM RatingEvent as re " +
            "WHERE event.id = ?1 " +
            "GROUP BY event.id")
    RatingDto findRating(Long eventId);

    @Query("SELECT new ru.practicum.ewm.dto.RatingDto(event.initiator.id, " +
            "SUM(CASE re.state WHEN 'LIKE' THEN 1 ELSE 0 END) as likes, " +
            "SUM(CASE re.state WHEN 'DISLIKE' THEN 1 ELSE 0 END) as dislikes) " +
            "FROM RatingEvent as re " +
            "WHERE event.initiator.id = ?1 " +
            "GROUP BY event.initiator.id")
    RatingDto findUserRating(Long eventId);

    Long countByEventIdAndState(Long eventId, StateAssessment like);


    @Query("SELECT new ru.practicum.ewm.dto.RatingEventShortDto(re.event, " +
            "SUM(CASE re.state WHEN 'LIKE' THEN 1 ELSE 0 END) as likes, " +
            "SUM(CASE re.state WHEN 'DISLIKE' THEN 1 ELSE 0 END) as dislikes) " +
            "FROM RatingEvent as re " +
            "WHERE re.state = 'LIKE' OR re.state = 'DISLIKE' " +
            "GROUP BY re.event.id " +
            "ORDER BY likes DESC")
    Page<RatingEventShortDto> findAllByLikes(Pageable pageable);

    @Query("SELECT new ru.practicum.ewm.dto.RatingEventShortDto(re.event, " +
            "SUM(CASE re.state WHEN 'LIKE' THEN 1 ELSE 0 END) as likes, " +
            "SUM(CASE re.state WHEN 'DISLIKE' THEN 1 ELSE 0 END) as dislikes) " +
            "FROM RatingEvent as re " +
            "WHERE re.state = 'LIKE' OR re.state = 'DISLIKE' " +
            "GROUP BY re.event.id " +
            "ORDER BY dislikes DESC")
    Page<RatingEventShortDto> findAllByDislikes(Pageable pageable);
}
