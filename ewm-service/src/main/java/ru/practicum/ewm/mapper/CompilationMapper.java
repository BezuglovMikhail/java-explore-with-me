package ru.practicum.ewm.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.request.UpdateCompilationRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@UtilityClass
public class CompilationMapper {

    public Compilation toCompilation(NewCompilationDto newCompilationDto, Set<Event> events) {
        Compilation newCompilation = new Compilation();
        newCompilation.setPinned(newCompilationDto.isPinned());
        newCompilation.setTitle(newCompilationDto.getTitle());
        newCompilation.setEvents(events);
        return newCompilation;
    }


    public CompilationDto toCompilationDto(Compilation compilation, HashMap<Long, Integer> eventsConfirmedRequest) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        if (compilation.getEvents() != null) {
            compilationDto.setEvents(EventMapper.mapToEventShortDto(compilation.getEvents(), eventsConfirmedRequest));
        }
        return compilationDto;
    }

    public Compilation toCompilationUpdate(UpdateCompilationRequest updateCompilationRequest, long compId,
                                           Compilation old, Set<Event> events) {
        Compilation updateCompilation = new Compilation();
        updateCompilation.setId(compId);
        updateCompilation.setPinned(updateCompilationRequest.getPinned() != null
                ? updateCompilationRequest.getPinned()
                : old.getPinned());
        updateCompilation.setTitle(updateCompilationRequest.getTitle() != null
                ? updateCompilationRequest.getTitle()
                : old.getTitle());
        updateCompilation.setEvents(events != null
                ? events : old.getEvents());
        return updateCompilation;
    }

    public List<CompilationDto> mapToCompilationDto(
            Iterable<Compilation> compilations,
            HashMap<Long, HashMap<Long, Integer>> confirmedRequestForCompilations) {
        List<CompilationDto> result = new ArrayList<>();

        for (Compilation compilation : compilations) {
            result.add(toCompilationDto(compilation, confirmedRequestForCompilations.get(compilation.getId())));
        }
        return result;
    }
}
