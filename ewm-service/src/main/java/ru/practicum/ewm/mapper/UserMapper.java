package ru.practicum.ewm.mapper;
import lombok.experimental.UtilityClass;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.dto.UserShortDto;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.request.NewUserRequest;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class UserMapper {

    public UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public UserShortDto toUserShortDto(User user) {
        UserShortDto userShortDto = new UserShortDto();
        userShortDto.setId(user.getId());
        userShortDto.setName(user.getName());
        return userShortDto;
    }

    public UserShortDto toUserShortDto(UserDto userDto) {
        UserShortDto userShortDto = new UserShortDto();
        userShortDto.setId(userDto.getId());
        userShortDto.setName(userDto.getName());
        return userShortDto;
    }

    public User toUser(NewUserRequest newUserRequest) {
        User newUser = new User();
        newUser.setName(newUserRequest.getName());
        newUser.setEmail(newUserRequest.getEmail());
        return newUser;
    }

    public List<UserDto> mapToUserDto(Iterable<User> users) {
        List<UserDto> result = new ArrayList<>();

        for (User user : users) {
            result.add(toUserDto(user));
        }
        return result;
    }
}
