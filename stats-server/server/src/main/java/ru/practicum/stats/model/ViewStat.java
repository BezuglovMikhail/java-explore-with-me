package ru.practicum.stats.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewStat {

    private String app;

    private String uri;

    private Long hits;

    @Override
    public String toString() {
        return "ViewStat{" +
                "app='" + app + '\'' +
                ", uri='" + uri + '\'' +
                ", hits=" + hits +
                '}';
    }
}
