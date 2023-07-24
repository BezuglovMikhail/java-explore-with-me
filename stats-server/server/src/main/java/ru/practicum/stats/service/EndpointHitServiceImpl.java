package ru.practicum.stats.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.stats.dto.EndpointHitDto;

import ru.practicum.stats.dtoStat.ViewStatDto;
import ru.practicum.stats.model.ViewStat;
import ru.practicum.stats.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.stats.dtoStat.EndpointHitMapper.toEndpointHit;
import static ru.practicum.stats.dtoStat.EndpointHitMapper.toEndpointHitDto;
import static ru.practicum.stats.dtoStat.ViewStatMapper.mapToViewStatDto;

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
        List<ViewStat> viewStatList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startFormatter = LocalDateTime.parse(start, formatter);
        LocalDateTime endFormatter = LocalDateTime.parse(end, formatter);

        if (uris == null) {
            viewStatList = repository.findAll(startFormatter, endFormatter);
        } else if (unique) {
            for (String uri : uris) {
                viewStatList.add(repository.getDistinctViewStat(uri, startFormatter, endFormatter));
            }
        } else {
            for (String uri : uris) {
                viewStatList.add(repository.getViewStat(uri, startFormatter, endFormatter));
            }
        }
        return mapToViewStatDto(viewStatList)
                .stream()
                .sorted((o1, o2) -> (int) (o2.getHits() - o1.getHits()))
                .collect(Collectors.toList());
    }
}
