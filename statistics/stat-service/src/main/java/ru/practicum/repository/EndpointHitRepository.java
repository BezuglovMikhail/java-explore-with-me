package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.ViewStatDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStat;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ViewStat(eh.app, eh.uri, COUNT(eh.ip)) FROM endpoint_hits as eh " +
            "WHERE eh.uri = '%' AND eh.timestamp BETWEEN '%' AND '%' " +
            "GROUP BY eh.app, eh.uri")
    ViewStat getViewStat(String uri);
    //ViewStat findCountIpAsHitsByUriAndTimestampBetween(String uri, LocalDateTime start, LocalDateTime end);

    /*@Query("SELECT new ViewStat(eh.app, eh.uri, COUNT(Distinct eh.ip)) FROM endpoint_hits as eh " +
            "WHERE eh.uri = '%' AND eh.timestamp BETWEEN '%' AND '%' " +
            "GROUP BY eh.app, eh.uri")
    ViewStat getDistinctViewStat(String uri, LocalDateTime start, LocalDateTime end);*/

    /*@Query("SELECT new ru.practicum.model.ViewStat(eh.app, eh.uri, COUNT(eh.ip)) FROM endpoint_hits as eh " +
           "WHERE eh.timestamp BETWEEN '%' AND '%' " +
            "GROUP BY eh.app, eh.uri")*/
    //List<ViewStat> findAll(LocalDateTime start, LocalDateTime end);
        //List<ViewStat> findAllCountIpAsHitsAppUriCountIpByTimestampBetween(LocalDateTime startFormatter, LocalDateTime endFormatter);

}
