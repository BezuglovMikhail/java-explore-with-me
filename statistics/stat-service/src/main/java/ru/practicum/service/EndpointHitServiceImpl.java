package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatDto;
import ru.practicum.model.ViewStat;
import ru.practicum.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.dto.EndpointHitMapper.toEndpointHit;
import static ru.practicum.dto.EndpointHitMapper.toEndpointHitDto;
import static ru.practicum.dto.ViewStatMapper.mapToViewStatDto;

@Service
public class EndpointHitServiceImpl implements EndpointHitService {

    @Autowired
    private final EndpointHitRepository repository;

    public EndpointHitServiceImpl(EndpointHitRepository endpointHitRepository) {
        this.repository = endpointHitRepository;
    }

    @Override
    public EndpointHitDto save(EndpointHitDto endpointHitDto) {
        return toEndpointHitDto(repository.save(toEndpointHit(endpointHitDto)));
    }

    @Override
    public List<ViewStatDto> findAllViewStats(List<String> uris, String start, String end, Boolean unique) {
      /* List<ViewStat> viewStatList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startFormatter = LocalDateTime.parse(start, formatter);
        LocalDateTime endFormatter = LocalDateTime.parse(end, formatter);

        if (uris == null) {
            //viewStatList = repository.findAllCountIpAsHitsAppUriCountIpByTimestampBetween(startFormatter, endFormatter);
        }

        else if (unique) {
            for (String uri : uris) {
                viewStatList.add(repository.findDistinctViewStat(uri, startFormatter, endFormatter));
            }
        } else {
            for (String uri : uris) {
                viewStatList.add(repository.findViewStat(uri, startFormatter, endFormatter));
            }
        }
        return mapToViewStatDto(viewStatList);*/
        return null;
    }
}
