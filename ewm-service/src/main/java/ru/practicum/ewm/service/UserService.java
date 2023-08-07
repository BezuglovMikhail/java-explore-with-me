package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.request.NewUserRequest;

import java.util.List;

public interface UserService {
    UserDto save(NewUserRequest newUserRequest);

    List<UserDto> findUsers(List<Long> ids, Integer from, Integer size);

    void deleteUser(long userId);
}
