package ru.practicum.stats.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ViewStatDto implements Serializable {

    @JsonProperty("app")
    private String app;
    @JsonProperty("uri")
    private String uri;
    @JsonProperty("hits")
    private Long hits;

    @Override
    public String toString() {
        return "ViewStatDto{" +
                "app:'" + app + '\'' +
                ", uri:'" + uri + '\'' +
                ", hits:" + hits +
                '}';
    }
}
