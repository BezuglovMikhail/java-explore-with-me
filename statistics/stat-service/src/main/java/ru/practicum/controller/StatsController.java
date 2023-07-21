package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatDto;
import ru.practicum.service.EndpointHitService;

import java.util.List;

@RestController
@Slf4j
public class StatsController {

    @Autowired
    private final EndpointHitService service;

    public StatsController(EndpointHitService service) {
        this.service = service;
    }

    @GetMapping(path = "/{stats}")
    public List<ViewStatDto> getStatisticsVisits(@RequestParam(required = false) List<String> uris, @RequestParam String start,
                                                 @RequestParam String end, @RequestParam(required = false, defaultValue = "false") Boolean unique) {


        List<ViewStatDto> viewStatDtoList = service.findAllViewStats(uris, start, end, unique);
        log.info("Statistics collected");

        return viewStatDtoList;
    }

    @PostMapping(path = "/{hit}")
    public EndpointHitDto saveUser(@RequestBody EndpointHitDto endpointHitDto) {
        EndpointHitDto addEndpointHit = service.save(endpointHitDto);
        log.info("Request Post received to add user: " + addEndpointHit);
        return addEndpointHit;
    }
}
