package ru.practicum.stats.dtoStat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewStatDto {
    private String app;

    private String uri;

    private Long hits;
}
