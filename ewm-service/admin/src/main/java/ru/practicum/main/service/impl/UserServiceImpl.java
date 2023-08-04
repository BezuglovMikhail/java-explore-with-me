package ru.practicum.main.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.main.CustomPageRequest;
import ru.practicum.main.dto.UserDto;
import ru.practicum.main.exeption.NotFoundException;
import ru.practicum.main.exeption.ValidationException;
import ru.practicum.main.model.User;
import ru.practicum.main.repository.UserRepository;
import ru.practicum.main.request.NewUserRequest;
import ru.practicum.main.service.UserService;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ru.practicum.main.mapper.UserMapper.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }


    @Override
    public UserDto save(NewUserRequest newUserRequest) {
        try {
            return toUserDto(repository.save(toUser(newUserRequest)));
        } catch (ConstraintViolationException e) {
            throw new ValidationException(e.getClass().getName(), e.getMessage(), e.getMessage(),
                    HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
    }

    @Override
    public List<UserDto> findUsers(List<Long> ids, Integer from, Integer size) {
        Sort sort = Sort.by("id").ascending();
        CustomPageRequest pageable = CustomPageRequest.by(from, size, sort);
        Page<User> page = Objects.nonNull(ids) && !ids.isEmpty()
                ? repository.findByIdIn(ids, pageable)
                : repository.findAll(pageable);

        return mapToUserDto(page.getContent());
    }

    @Override
    public void deleteUser(long userId) {
        repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User whit id = " + userId + " not found in database."));
        repository.deleteById(userId);
    }
}
