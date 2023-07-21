package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStat;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ru.practicum.model.ViewStat(eh.app as app, eh.uri as uri, COUNT(eh.ip) as hits) FROM EndpointHit as eh " +
            "WHERE eh.uri = ?1 AND eh.timestamp BETWEEN ?2 AND ?3 " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(eh.ip) DESC")
    ViewStat getViewStat(String uri, LocalDateTime start, LocalDateTime end);
    //ViewStat findCountIpAsHitsByUriAndTimestampBetween(String uri, LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.model.ViewStat(eh.app as app, eh.uri as uri, COUNT(DISTINCT eh.ip) as hits) FROM EndpointHit as eh " +
            "WHERE eh.uri = ?1 AND eh.timestamp BETWEEN ?2 AND ?3 " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(DISTINCT eh.ip) DESC")
    ViewStat getDistinctViewStat(String uri, LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.model.ViewStat(eh.app as app, eh.uri as uri, COUNT(eh.ip) as hits) FROM EndpointHit as eh " +
           "WHERE eh.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(eh.ip) DESC")
    List<ViewStat> findAll(LocalDateTime start, LocalDateTime end);
        //List<ViewStat> findAllCountIpAsHitsAppUriCountIpByTimestampBetween(LocalDateTime startFormatter, LocalDateTime endFormatter);

}
