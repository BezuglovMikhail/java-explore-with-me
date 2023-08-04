package ru.practicum.main.mapper;

import ru.practicum.main.dto.CompilationDto;
import ru.practicum.main.dto.NewCompilationDto;
import ru.practicum.main.model.Compilation;
import ru.practicum.main.request.UpdateCompilationRequest;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.main.mapper.EventMapper.mapToEventShortDto;

public class CompilationMapper {

    private CompilationMapper() {
    }

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        Compilation newCompilation = new Compilation();
        newCompilation.setPinned(newCompilationDto.isPinned());
        newCompilation.setTitle(newCompilationDto.getTitle());

        return newCompilation;
    }


    public static CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.isPinned());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setEvents(mapToEventShortDto(compilation.getEvents()));

        return compilationDto;
    }

    public static Compilation toCompilationUpdate(UpdateCompilationRequest updateCompilationRequest, long compId) {
        Compilation updateCompilation = new Compilation();
        updateCompilation.setId(compId);
        updateCompilation.setPinned(updateCompilationRequest.isPinned());
        updateCompilation.setTitle(updateCompilationRequest.getTitle());

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
