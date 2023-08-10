package ru.practicum.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.service.CategoryService;
import ru.practicum.ewm.service.CompilationService;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.status.EventSort;
import ru.practicum.stats.client.StatClient;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@Slf4j
public class PublicController {

    private CompilationService compilationService;

    private CategoryService categoryService;

    private EventService eventService;

    private final StatClient statClient;

    public PublicController(CompilationService compilationService, CategoryService categoryService,
                            EventService eventService, StatClient statClient) {
        this.compilationService = compilationService;
        this.categoryService = categoryService;
        this.eventService = eventService;
        this.statClient = statClient;
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size,
                                                @RequestParam(name = "pinned", defaultValue = "false") boolean pinned) {
        log.info("Request Get received whit parameter pinned = {}" +
                " to find list compilations ", pinned);
        List<CompilationDto> compilationDto = compilationService.findCompilationsPinned(pinned, from, size);
        return compilationDto;
    }

    @GetMapping("/compilations/{compilationId}")
    public CompilationDto getCompilationDtoById(@PathVariable("compilationId") Long compilationId) {
        CompilationDto compilationDto = compilationService.getCompilationById(compilationId);
        log.info("Request Get received to compilation whit id = {}", compilationId);
        return compilationDto;
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        log.info("Request Get received to find list categories ");
        List<CategoryDto> categoryDtoList = categoryService.findCategories(from, size);
        return categoryDtoList;
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryDtoById(@PathVariable("catId") Long catId) {
        CategoryDto compilationDto = categoryService.getCategoryById(catId);
        log.info("Request Get received to category whit id = {}", catId);
        return compilationDto;
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<Object> getEventById(HttpServletRequest servletRequest, @PathVariable("id") Long id) {
        EventFullDto body = eventService.getEventById(id);
        log.info("Request Get received to category whit id = {}", id);

        makeToStats(servletRequest);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventShortDto>> publicGetEventsWhitFilters(HttpServletRequest servletRequest,
                                                                          @RequestParam(required = false) String text,
                                                                          @RequestParam(required = false) Boolean paid,
                                                                          @RequestParam(required = false) Boolean onlyAvailable,
                                                                          @RequestParam(required = false) EventSort sort,
                                                                          @RequestParam(required = false) List<Long> users,
                                                                          @RequestParam(required = false) List<Long> categories,
                                                                          @RequestParam(required = false)
                                                                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                                          LocalDateTime rangeStart,
                                                                          @RequestParam(required = false)
                                                                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                                          LocalDateTime rangeEnd,
                                                                          @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                                          @Positive @RequestParam(defaultValue = "10") Integer size) {

        log.debug("Request Get received whit parameters: text: {}, paid: {}, onlyAvailable: {}, sort: {}, " +
                        "userIds: {}, states: {}, categoryIds: {}, rangeStart: {}, rangeEnd {}, from: {}, size: {}",
                text, paid, onlyAvailable, sort, users, categories, rangeStart, rangeEnd, from, size);

        makeToStats(servletRequest);

        List<EventShortDto> body = eventService.publicFindEventsWhitFilter(text, paid, onlyAvailable, sort, users, categories,
                rangeStart, rangeEnd, from, size);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    private void makeToStats(HttpServletRequest servletRequest) {
        log.debug("Sending request to stats");
        try {
            statClient.createStatistics(servletRequest);
        } catch (RestClientException e) {
            log.error("Can't connect to the statistics server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
