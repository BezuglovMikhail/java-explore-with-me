package ru.practicum.main.service;

import ru.practicum.main.dto.CompilationDto;
import ru.practicum.main.dto.NewCompilationDto;
import ru.practicum.main.request.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    CompilationDto save(NewCompilationDto newCompilationDto);

    void deleteComp(long compId);

    CompilationDto updateCompilation(UpdateCompilationRequest updateCompilationRequest, long compId);

    CompilationDto getCompilationById(Long compilationId);

    List<CompilationDto> findCompilationsPinned(boolean pinned, Integer from, Integer size);

}
