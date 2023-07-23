package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.Client;
import ru.practicum.dto.EndpointHitDto;

import javax.validation.Valid;
import java.util.List;

@org.springframework.stereotype.Controller
@RequiredArgsConstructor
@Slf4j
@Validated
public class Controller {
    private final Client client;

    @GetMapping("/stats")
    public ResponseEntity<Object> getStatistics(@RequestParam String start,
                                                   @RequestParam String end,
                                                   @RequestParam(required = false) List<String> uris,
                                                   @RequestParam(required = false,
                                                           defaultValue = "false") Boolean unique) {

        log.info("Request Get received to find list all hitsStatistics" +
                " at time between {} and {}", start, end);
        return client.getStatistics(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestBody @Valid EndpointHitDto requestDto) {
        log.info("Creating hit {}", requestDto);
        return client.createStatistics(requestDto);
    }
}
