package ru.practicum.ewm.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.until.CustomPageRequest;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.exeption.NotFoundException;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.request.NewUserRequest;
import ru.practicum.ewm.service.UserService;

import java.util.List;
import java.util.Objects;

import static ru.practicum.ewm.mapper.UserMapper.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public UserDto save(NewUserRequest newUserRequest) {
        return toUserDto(repository.save(toUser(newUserRequest)));
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

    @Transactional
    @Override
    public void deleteUser(long userId) {
        repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User whit id = " + userId + " not found in database."));
        repository.deleteById(userId);
    }
}
