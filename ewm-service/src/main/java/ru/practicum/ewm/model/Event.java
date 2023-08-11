package ru.practicum.ewm.model;

import lombok.*;
import ru.practicum.ewm.status.State;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 20, max = 2000)
    private String annotation;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @Column(name = "confirmed_requests", columnDefinition = "BIGINT DEFAULT 0", nullable = false)
    private Integer confirmedRequests;

    private LocalDateTime createdOn;

    @Size(min = 20, max = 7000)
    private String description;

    private LocalDateTime eventDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    private User initiator;

    //@ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "location_id", referencedColumnName = "id")
    @Embedded
    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private State state;

    @Size(min = 3, max = 120)
    private String title;

    @Transient
    private Long views = 0L;
}
