package ru.practicum.ewm.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.request.NewUserRequest;
import ru.practicum.ewm.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@Slf4j
public class AdminUserController {

    @Autowired
    UserService userService;

    @GetMapping
    public List<UserDto> getUsers(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                  @Positive @RequestParam(defaultValue = "10") Integer size,
                                  @RequestParam(name = "ids", required = false) List<Long> ids) {
        log.info("Request Get received whit parameter ids = {}" +
                " to find list user ", ids);
        return userService.findUsers(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDto addUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        UserDto addedUser = userService.save(newUserRequest);
        log.info("Request Post received to add user: {}", addedUser);
        return addedUser;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
        log.info("Request Delete received to user delete, userDeleteId = {} ", userId);
    }
}
