package ru.practicum.ewm.controller.pub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@Slf4j
public class PublicCompilationController {

    @Autowired
    private CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size,
                                                @RequestParam(name = "pinned", required = false) Boolean pinned) {
        log.info("Request Get received whit parameter pinned = {}" +
                " to find list compilations ", pinned);
        List<CompilationDto> compilationDto = compilationService.findCompilationsPinned(pinned, from, size);
        return compilationDto;
    }

    @GetMapping("/{compilationId}")
    public CompilationDto getCompilationDtoById(@PathVariable("compilationId") Long compilationId) {
        CompilationDto compilationDto = compilationService.getCompilationById(compilationId);
        log.info("Request Get received to compilation whit id = {}", compilationId);
        return compilationDto;
    }
}
