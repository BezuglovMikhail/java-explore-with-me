package ru.practicum.dto;

import ru.practicum.model.EndpointHit;

public class EndpointHitMapper {

    public static EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        if (endpointHit != null) {
            return new EndpointHitDto(
                    endpointHit.getId(),
                    endpointHit.getApp(),
                    endpointHit.getUri(),
                    endpointHit.getIp(),
                    endpointHit.getTimestamp()
            );
        } else {
            return null;
        }
    }

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        if (endpointHitDto != null) {
            return new EndpointHit(
                    endpointHitDto.getId(),
                    endpointHitDto.getApp(),
                    endpointHitDto.getUri(),
                    endpointHitDto.getIp(),
                    endpointHitDto.getTimestamp()
            );
        } else {
            return null;
        }
    }
}
