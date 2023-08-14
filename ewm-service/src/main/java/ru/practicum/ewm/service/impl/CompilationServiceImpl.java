package ru.practicum.ewm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import ru.practicum.ewm.until.ConfirmedRequests;
import ru.practicum.ewm.until.CustomPageRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CompilationServiceImpl implements CompilationService {

    private CompilationRepository compilationRepository;

    private EventRepository eventRepository;

    @Autowired
    private final ConfirmedRequests confirmedRequests;

    public CompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository,
                                  ConfirmedRequests confirmedRequests) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
        this.confirmedRequests = confirmedRequests;
    }


    @Transactional
    @Override
    public CompilationDto save(NewCompilationDto newCompilationDto) {
        Set<Event> events = new HashSet<>();
        if (newCompilationDto.getEvents() != null) {
            events = eventRepository.findByIdIn(newCompilationDto.getEvents());
        }

        Compilation newCompilation = compilationRepository.save(CompilationMapper.toCompilation(newCompilationDto,
                events));
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
        Set<Event> events = new HashSet<>();
        if (updateCompilationRequest.getEvents() != null) {
            events = eventRepository.findByIdIn(updateCompilationRequest.getEvents());
            return CompilationMapper.toCompilationDto(compilationRepository
                    .save(CompilationMapper.toCompilationUpdate(updateCompilationRequest, compId, old, events)));
        }

        return CompilationMapper.toCompilationDto(compilationRepository
                .save(CompilationMapper.toCompilationUpdate(updateCompilationRequest, compId, old, events)));
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation whit id = " + compId + " not found in database."));
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    public List<CompilationDto> findCompilationsPinned(Boolean pinned, Integer from, Integer size) {
        Sort sort = Sort.by("id").ascending();
        CustomPageRequest pageable = CustomPageRequest.by(from, size, sort);

        Page<Compilation> page;

        if (pinned != null) {
            page = compilationRepository.findAllByPinned(pinned, pageable);
        } else {
            page = compilationRepository.findAll(pageable);
        }

        return CompilationMapper.mapToCompilationDto(page.getContent());
    }
}
