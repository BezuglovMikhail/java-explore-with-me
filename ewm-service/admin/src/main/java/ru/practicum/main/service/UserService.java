package ru.practicum.main.service;

import ru.practicum.main.dto.UserDto;
import ru.practicum.main.request.NewUserRequest;

import java.util.List;

public interface UserService {
    UserDto save(NewUserRequest newUserRequest);

    List<UserDto> findUsers(List<Long> ids, Integer from, Integer size);

    void deleteUser(long userId);
}
