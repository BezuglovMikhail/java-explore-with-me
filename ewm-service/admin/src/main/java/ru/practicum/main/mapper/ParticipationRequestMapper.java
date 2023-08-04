package ru.practicum.main.mapper;

import ru.practicum.main.dto.ParticipationRequestDto;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.ParticipationRequest;
import ru.practicum.main.model.User;
import ru.practicum.main.status.State;

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
        participationRequestDto.setStatus(partRequest.getStatus());

        return participationRequestDto;
    }

    public static ParticipationRequest toParticipationRequest(Event event, User requester) {
        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setCreated(LocalDateTime.now());
        participationRequest.setEvent(event);
        participationRequest.setRequester(requester);
        participationRequest.setStatus(State.PENDING);

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
