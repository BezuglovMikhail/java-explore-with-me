package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.newdto.NewCompilationDto;
import ru.practicum.ewm.request.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    CompilationDto save(NewCompilationDto newCompilationDto);

    void deleteComp(long compId);

    CompilationDto updateCompilation(UpdateCompilationRequest updateCompilationRequest, long compId);

    CompilationDto getCompilationById(Long compilationId);

    List<CompilationDto> findCompilationsPinned(Boolean pinned, Integer from, Integer size);
}
