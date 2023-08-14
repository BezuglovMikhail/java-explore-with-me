package ru.practicum.ewm.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Location {

    private float lat;

    private float lon;
}
