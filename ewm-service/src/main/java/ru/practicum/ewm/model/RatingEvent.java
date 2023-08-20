package ru.practicum.ewm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.until.status.StateAssessment;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "rating_events")
public class RatingEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "appraiser_id", referencedColumnName = "id")
    private User appraiser;

    @Enumerated(EnumType.STRING)
    private StateAssessment state;

    @Transient
    private Long likes;

    @Transient
    private Long dislikes;
}
