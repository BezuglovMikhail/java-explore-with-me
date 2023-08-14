package ru.practicum.stats.dtoStat;

import ru.practicum.stats.dto.ViewStatDto;
import ru.practicum.stats.model.ViewStat;

import java.util.ArrayList;
import java.util.List;

public class ViewStatMapper {

    private ViewStatMapper() {
    }

    private static ViewStatDto toViewStartDto(ViewStat viewStat) {
        return new ViewStatDto(
                viewStat.getApp(),
                viewStat.getUri(),
                viewStat.getHits()
        );
    }

    public static List<ViewStatDto> mapToViewStatDto(Iterable<ViewStat> viewStats) {
        List<ViewStatDto> result = new ArrayList<>();
        for (ViewStat viewStat : viewStats) {
            result.add(toViewStartDto(viewStat));
        }
        return result;
    }
}
