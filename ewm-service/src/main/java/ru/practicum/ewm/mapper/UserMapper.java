package ru.practicum.ewm.mapper;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.dto.UserShortDto;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.request.NewUserRequest;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    private UserMapper() {
    }

    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static UserShortDto toUserShortDto(User user) {
        UserShortDto userShortDto = new UserShortDto();
        userShortDto.setId(user.getId());
        userShortDto.setName(user.getName());
        return userShortDto;
    }

    public static UserShortDto toUserShortDto(UserDto userDto) {
        UserShortDto userShortDto = new UserShortDto();
        userShortDto.setId(userDto.getId());
        userShortDto.setName(userDto.getName());
        return userShortDto;
    }

    public static User toUser(NewUserRequest newUserRequest) {
        User newUser = new User();
        newUser.setName(newUserRequest.getName());
        newUser.setEmail(newUserRequest.getEmail());

        return newUser;
    }

    public static List<UserDto> mapToUserDto(Iterable<User> users) {
        List<UserDto> result = new ArrayList<>();

        for (User user : users) {
            result.add(toUserDto(user));
        }

        return result;
    }
}
