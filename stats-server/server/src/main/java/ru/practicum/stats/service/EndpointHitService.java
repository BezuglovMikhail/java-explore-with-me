package ru.practicum.stats.service;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dtoStat.ViewStatDto;

import java.util.List;

public interface EndpointHitService {

    EndpointHitDto save(EndpointHitDto endpointHitDto);

    List<ViewStatDto> findAllViewStats(List<String> uris, String start, String end, Boolean unique);
}
