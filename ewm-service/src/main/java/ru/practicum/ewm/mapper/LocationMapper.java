package ru.practicum.ewm.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.dto.LocationDto;
import ru.practicum.ewm.model.Location;


@UtilityClass
public class LocationMapper {

    public Location toLocation(LocationDto locationDto) {
        Location location = new Location();
        location.setLat(locationDto.getLat());
        location.setLon(locationDto.getLon());
        return location;
    }

    public LocationDto toLocationDto(Location location) {
        LocationDto locationDto = new LocationDto();
        locationDto.setLat(location.getLat());
        locationDto.setLon(location.getLon());
        return locationDto;
    }
}
