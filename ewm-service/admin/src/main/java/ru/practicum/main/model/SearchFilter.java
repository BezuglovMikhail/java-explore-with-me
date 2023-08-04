package ru.practicum.main.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.main.status.State;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Builder
public class SearchFilter {
    List<Long> users;
    List<State> states;
    List<Long> categories;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime rangeStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime rangeEnd;

    String text;
    Boolean paid;
    Boolean onlyAvailable;

    public Specification<Event> makeSpecification() {
        return Specification
                .where(eventAnnotationOrDescriptionContains(text))
                .and(eventHasOneOfCategory(categories))
                .and(eventIsPaid(paid))
                .and(eventStartIsBetween(rangeStart, rangeEnd))
                .and(eventCreatorIsIn(users))
                .and(eventStatusIsIn(states));
    }

    private static Specification<Event> eventHasOneOfCategory(Collection<Long> ids) {
        return (root, query, builder) -> {
            if (ids == null || ids.size() == 0) {
                return builder.and();
            }

            return root.get(ru.practicum.main.model.Event_.CATEGORY).in(ids);
        };
    }

    private static Specification<Event> eventCreatorIsIn(Collection<Long> ids) {
        return (root, query, builder) -> {
            if (ids == null || ids.size() == 0) {
                return builder.and();
            }

            return root.get(ru.practicum.main.model.Event_.INITIATOR).in(ids);
        };
    }

    private static Specification<Event> eventStatusIsIn(Collection<State> statuses) {
        return (root, query, builder) -> {
            if (statuses == null || statuses.size() == 0) {
                return builder.and();
            }

            return root.get(ru.practicum.main.model.Event_.STATE).in(statuses);
        };
    }

    private static Specification<Event> eventAnnotationOrDescriptionContains(String expression) {
        return (root, query, builder) -> {
            if (expression == null) {
                return builder.and();
            }

            return builder.or(
                    builder.like(builder.lower(root.get(ru.practicum.main.model.Event_.ANNOTATION)), contains(expression)),
                    builder.like(builder.lower(root.get(ru.practicum.main.model.Event_.DESCRIPTION)), contains(expression))
            );
        };
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression.toLowerCase());
    }

    private static Specification<Event> eventIsPaid(Boolean paid) {
        return (root, query, builder) -> {
            if (paid == null) {
                return builder.and();
            }

            return builder.equal(root.get(ru.practicum.main.model.Event_.PAID), paid);
        };
    }

    private static Specification<Event> eventStartIsBetween(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        return (root, query, builder) -> {

            LocalDateTime queryRangeStart = rangeStart != null ? rangeStart : LocalDateTime.now();
            LocalDateTime queryRangeEnd = rangeEnd;

            return builder.and(
                    builder.greaterThan(root.get(ru.practicum.main.model.Event_.EVENT_DATE), queryRangeStart),
                    queryRangeEnd != null ? builder.lessThan(root.get(ru.practicum.main.model.Event_.EVENT_DATE), queryRangeEnd) : builder.and()
            );
        };
    }
}
