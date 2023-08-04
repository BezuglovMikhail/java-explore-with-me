package ru.practicum.main.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.main.CustomPageRequest;
import ru.practicum.main.dto.CompilationDto;
import ru.practicum.main.dto.NewCompilationDto;
import ru.practicum.main.exeption.NotFoundException;
import ru.practicum.main.model.Compilation;
import ru.practicum.main.repository.CompilationRepository;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.request.UpdateCompilationRequest;
import ru.practicum.main.service.CompilationService;

import java.util.List;

import static ru.practicum.main.mapper.CompilationMapper.*;

@Service
@Slf4j
public class CompilationServiceImpl implements CompilationService {

    @Autowired
    private CompilationRepository compilationRepository;

    @Autowired
    private EventRepository eventRepository;

    public CompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public CompilationDto save(NewCompilationDto newCompilationDto) {

        Compilation newCompilation = compilationRepository.save(toCompilation(newCompilationDto));
        CompilationDto compilationDto = toCompilationDto(newCompilation);

        return compilationDto;
    }

    @Override
    public void deleteComp(long compId) {
        compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation whit id = " + compId + " not found in database."));
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilation(UpdateCompilationRequest updateCompilationRequest, long compId) {
        compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation whit id = " + compId + " not found in database."));
        return toCompilationDto(compilationRepository
                .save(toCompilationUpdate(updateCompilationRequest, compId)));
    }

    @Override
    public CompilationDto getCompilationById(Long compilationId) {
        return toCompilationDto(compilationRepository.findById(compilationId).get());
    }

    @Override
    public List<CompilationDto> findCompilationsPinned(boolean pinned, Integer from, Integer size) {
        Sort sort = Sort.by("id").ascending();
        CustomPageRequest pageable = CustomPageRequest.by(from, size, sort);
        Page<Compilation> page = compilationRepository.findAllByPinned(pinned, pageable);

        return mapToCompilationDto(page.getContent());
    }
}
