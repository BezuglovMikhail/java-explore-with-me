package ru.practicum.stats.dtoStat;
import ru.practicum.stats.model.ViewStat;

import java.util.ArrayList;
import java.util.List;

public class ViewStatMapper {

    public static ViewStatDto toViewStartDto(ViewStat viewStat) {
        return new ViewStatDto(
                viewStat.getApp(),
                viewStat.getUri(),
                viewStat.getHits()
        );
    }

    /*public static ViewStat toViewStart(ViewStatDto viewStatDto) {
        return new ViewStat(
                viewStatDto.getApp(),
                viewStatDto.getUri(),
                viewStatDto.getHits()
        );
    }*/

    public static List<ViewStatDto> mapToViewStatDto(Iterable<ViewStat> viewStats) {
        List<ViewStatDto> result = new ArrayList<>();

        for (ViewStat viewStat : viewStats) {
            result.add(toViewStartDto(viewStat));
        }
        return result;
    }
}