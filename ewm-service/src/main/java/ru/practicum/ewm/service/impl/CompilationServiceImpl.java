package ru.practicum.ewm.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.CustomPageRequest;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.exeption.NotFoundException;
import ru.practicum.ewm.mapper.CompilationMapper;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.request.UpdateCompilationRequest;
import ru.practicum.ewm.service.CompilationService;

import java.util.ArrayList;
import java.util.List;

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

    @Transactional
    @Override
    public CompilationDto save(NewCompilationDto newCompilationDto) {
        List<Event> events = new ArrayList<>();
        if (newCompilationDto.getEvents() != null) {
            events = eventRepository.findByIdIn(newCompilationDto.getEvents());
        }
        Compilation newCompilation = compilationRepository.save(CompilationMapper.toCompilation(newCompilationDto, events));
        return CompilationMapper.toCompilationDto(newCompilation);
    }

    @Transactional
    @Override
    public void deleteComp(long compId) {
        compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation whit id = " + compId + " not found in database."));
        compilationRepository.deleteById(compId);
    }

    @Transactional
    @Override
    public CompilationDto updateCompilation(UpdateCompilationRequest updateCompilationRequest, long compId) {
        Compilation old = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation whit id = " + compId + " not found in database."));
        List<Event> events = new ArrayList<>();
        if (updateCompilationRequest.getEvents() != null) {
            events = eventRepository.findByIdIn(updateCompilationRequest.getEvents());
        }
        return CompilationMapper.toCompilationDto(compilationRepository
                .save(CompilationMapper.toCompilationUpdate(updateCompilationRequest, compId, old, events)));
    }

    @Override
    public CompilationDto getCompilationById(Long compilationId) {
        return CompilationMapper.toCompilationDto(compilationRepository.findById(compilationId).get());
    }

    @Override
    public List<CompilationDto> findCompilationsPinned(boolean pinned, Integer from, Integer size) {
        Sort sort = Sort.by("id").ascending();
        CustomPageRequest pageable = CustomPageRequest.by(from, size, sort);
        Page<Compilation> page = compilationRepository.findAllByPinned(pinned, pageable);

        return CompilationMapper.mapToCompilationDto(page.getContent());
    }
}
