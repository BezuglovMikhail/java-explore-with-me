package ru.practicum.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatDto;

import java.util.List;

public interface EndpointHitService {

    EndpointHitDto save(EndpointHitDto endpointHitDto);

    List<ViewStatDto> findAllViewStats(List<String> uris, String start, String end, Boolean unique);
}
