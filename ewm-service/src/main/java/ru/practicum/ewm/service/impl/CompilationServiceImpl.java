package ru.practicum.ewm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.ConfirmedRequests;
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

import java.util.HashMap;
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
        HashMap<Long, Integer> confirmedReqMap = new HashMap<>();
        if (newCompilationDto.getEvents() != null) {
            events = eventRepository.findByIdIn(newCompilationDto.getEvents());
            confirmedReqMap = confirmedRequests.findConfirmedRequests(events);
        }

        Compilation newCompilation = compilationRepository.save(CompilationMapper.toCompilation(newCompilationDto,
                events));
        return CompilationMapper.toCompilationDto(newCompilation, confirmedReqMap);
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
        HashMap<Long, Integer> confirmedReqMap = new HashMap<>();
        if (updateCompilationRequest.getEvents() != null) {
            events = eventRepository.findByIdIn(updateCompilationRequest.getEvents());
            confirmedReqMap = confirmedRequests.findConfirmedRequests(events);
            return CompilationMapper.toCompilationDto(compilationRepository
                            .save(CompilationMapper.toCompilationUpdate(updateCompilationRequest, compId, old, events)),
                    confirmedReqMap);
        }

        confirmedReqMap = confirmedRequests.findConfirmedRequests(old.getEvents());
        return CompilationMapper.toCompilationDto(compilationRepository
                        .save(CompilationMapper.toCompilationUpdate(updateCompilationRequest, compId, old, events)),
                confirmedReqMap);
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation whit id = " + compId + " not found in database."));
        return CompilationMapper.toCompilationDto(compilation,
                confirmedRequests.findConfirmedRequests(compilation.getEvents()));
    }

    @Override
    public List<CompilationDto> findCompilationsPinned(boolean pinned, Integer from, Integer size) {
        Sort sort = Sort.by("id").ascending();
        CustomPageRequest pageable = CustomPageRequest.by(from, size, sort);
        Page<Compilation> page = compilationRepository.findAllByPinned(pinned, pageable);
        HashMap<Long, HashMap<Long, Integer>> confirmedRequestForCompilations = new HashMap<>();

        for (Compilation compilation : page.getContent()) {
            confirmedRequestForCompilations.put(compilation.getId(),
                    confirmedRequests.findConfirmedRequests(compilation.getEvents()));
        }
        return CompilationMapper.mapToCompilationDto(page.getContent(), confirmedRequestForCompilations);
    }
}
