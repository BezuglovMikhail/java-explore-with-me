package ru.practicum.ewm.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
//@Entity
@NoArgsConstructor
//@Table(name = "locations")
@Embeddable
public class Location {

    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Long id;

    private float lat;

    private float lon;
}
