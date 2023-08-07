package ru.practicum.ewm.mapper;

import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.request.UpdateCompilationRequest;

import java.util.ArrayList;
import java.util.List;

public class CompilationMapper {

    private CompilationMapper() {
    }

    public static Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        Compilation newCompilation = new Compilation();
        newCompilation.setPinned(newCompilationDto.getPinned());
        newCompilation.setTitle(newCompilationDto.getTitle());
        newCompilation.setEvents(events);
        return newCompilation;
    }


    public static CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        if (compilation.getEvents() != null) {
            compilationDto.setEvents(EventMapper.mapToEventShortDto(compilation.getEvents()));
        }
        return compilationDto;
    }

    public static Compilation toCompilationUpdate(UpdateCompilationRequest updateCompilationRequest, long compId,
                                                  Compilation old, List<Event> events) {
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

    public static List<CompilationDto> mapToCompilationDto(Iterable<Compilation> compilations) {
        List<CompilationDto> result = new ArrayList<>();

        for (Compilation compilation : compilations) {
            result.add(toCompilationDto(compilation));
        }

        return result;
    }
}
