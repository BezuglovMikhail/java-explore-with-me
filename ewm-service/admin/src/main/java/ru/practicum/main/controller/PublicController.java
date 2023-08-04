package ru.practicum.main.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.dto.CompilationDto;
import ru.practicum.main.dto.EventFullDto;
import ru.practicum.main.service.CategoryService;
import ru.practicum.main.service.CompilationService;
import ru.practicum.main.service.EventService;
import ru.practicum.main.status.EventSort;
import ru.practicum.main.status.State;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping
@Slf4j
public class PublicController {

    @Autowired
    private CompilationService compilationService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private EventService eventService;

    public PublicController(CompilationService compilationService, CategoryService categoryService, EventService eventService) {
        this.compilationService = compilationService;
        this.categoryService = categoryService;
        this.eventService = eventService;
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

    @GetMapping("/categories/{categoryId}")
    public CategoryDto getCategoryDtoById(@PathVariable("catId") Long catId) {
        CategoryDto compilationDto = categoryService.getCategoryById(catId);
        log.info("Request Get received to category whit id = {}", catId);
        return compilationDto;
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(@PathVariable("id") Long id) {
        EventFullDto eventFullDto = eventService.getEventById(id);
        log.info("Request Get received to category whit id = {}", id);
        return eventFullDto;
    }

    @GetMapping("/events")
    public List<EventFullDto> publicGetEventsWhitFilters(@RequestParam(required = false) String text,
                                                         @RequestParam(required = false) Boolean paid,
                                                         @RequestParam(required = false) Boolean onlyAvailable,
                                                         @RequestParam(required = false) EventSort sort,
                                                         @RequestParam(required = false) List<Long> users,
                                                         @RequestParam(required = false) List<State> states,
                                                         @RequestParam(required = false) List<Long> categories,
                                                         @RequestParam(required = false) String rangeStart,
                                                         @RequestParam(required = false) String rangeEnd,
                                                         @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                         @Positive @RequestParam(defaultValue = "10") Integer size) {

        log.debug("Request Get received whit parameters: text: {}, paid: {}, onlyAvailable: {}, sort: {}, " +
                        "userIds: {}, states: {}, categoryIds: {}, rangeStart: {}, rangeEnd {}, from: {}, size: {}",
                text, paid, onlyAvailable, sort, users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.publicFindEventsWhitFilter(text, paid, onlyAvailable, sort, users, states, categories,
                rangeStart, rangeEnd, from, size);
    }

}
