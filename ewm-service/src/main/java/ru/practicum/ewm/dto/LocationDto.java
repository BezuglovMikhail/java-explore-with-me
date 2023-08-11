package ru.practicum.ewm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class LocationDto {

    @NotNull
    private float lat;

    @NotNull
    private float lon;
}
