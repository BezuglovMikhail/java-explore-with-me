package ru.practicum.ewm.mapper;

import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.model.ParticipationRequest;
import ru.practicum.ewm.status.State;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ParticipationRequestMapper {

    private ParticipationRequestMapper() {
    }

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest partRequest) {
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
        participationRequestDto.setId(partRequest.getId());
        participationRequestDto.setCreated(partRequest.getCreated());
        participationRequestDto.setEvent(partRequest.getEvent().getId());
        participationRequestDto.setRequester(partRequest.getRequester().getId());
        participationRequestDto.setStatus(partRequest.getState());

        return participationRequestDto;
    }

    public static ParticipationRequest toParticipationRequest(Event event, User requester) {
        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setCreated(LocalDateTime.now());
        participationRequest.setEvent(event);
        participationRequest.setRequester(requester);

        participationRequest.setState(
                event.getRequestModeration().equals(false)
                        || event.getParticipantLimit() == 0
                        ? State.CONFIRMED
                        : State.PENDING);

        return participationRequest;
    }

    public static List<ParticipationRequestDto> mapToParticipationRequestDto(List<ParticipationRequest> participationRequests) {
        List<ParticipationRequestDto> result = new ArrayList<>();

        for (ParticipationRequest participationRequest : participationRequests) {
            result.add(toParticipationRequestDto(participationRequest));
        }
        return result;
    }
}
