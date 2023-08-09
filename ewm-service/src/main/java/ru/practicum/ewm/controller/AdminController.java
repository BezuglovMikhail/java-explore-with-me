package ru.practicum.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.*;
import ru.practicum.ewm.request.NewUserRequest;
import ru.practicum.ewm.request.UpdateCompilationRequest;
import ru.practicum.ewm.request.UpdateEventAdminRequest;
import ru.practicum.ewm.service.CategoryService;
import ru.practicum.ewm.service.CompilationService;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.service.UserService;
import ru.practicum.ewm.status.State;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.ewm.mapper.UserMapper.toUserShortDto;

@RestController
@RequestMapping(path = "/admin")
@Slf4j
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CompilationService compilationService;
    @Autowired
    private EventService eventService;

    public AdminController(UserService userService, CategoryService categoryService, CompilationService compilationService, EventService eventService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.compilationService = compilationService;
        this.eventService = eventService;
    }

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(defaultValue = "0") Integer from,
                                  @RequestParam(defaultValue = "10") Integer size,
                                  @RequestParam(name = "ids", required = false) List<Long> ids) {
        log.info("Request Get received whit parameter ids = {}" +
                " to find list user ", ids);
        List<UserDto> usersDto = userService.findUsers(ids, from, size);
        return usersDto;
    }

    @PostMapping("/users")
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDto addUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        UserDto addedUser = userService.save(newUserRequest);
        log.info("Request Post received to add user: " + toUserShortDto(addedUser));
        return addedUser;
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
        log.info("Request Delete received to user delete, userDeleteId = {} ", userId);
    }

    @PostMapping("/categories")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        CategoryDto addedCategory = categoryService.save(newCategoryDto);
        log.info("Request Post received to add category: " + addedCategory);
        return addedCategory;
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long catId) {
        categoryService.deleteCategory(catId);
        log.info("Request Delete received to category delete, categoryDeleteId = {} ", catId);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable Long catId) {
        CategoryDto updatedCategory = categoryService.updateCategory(categoryDto, catId);
        log.info("Request Update received to update user, updateUser: " + updatedCategory);
        return updatedCategory;
    }

    @PostMapping("/compilations")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        CompilationDto addedCompilation = compilationService.save(newCompilationDto);
        log.info("Request Post received to add compilation: " + addedCompilation);
        return addedCompilation;
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteComp(@PathVariable long compId) {
        compilationService.deleteComp(compId);
        log.info("Request Delete received to user delete, userDeleteId = {} ", compId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(@Valid @RequestBody UpdateCompilationRequest updateCompilationRequest, @PathVariable Long compId) {
        CompilationDto updateCompilation = compilationService.updateCompilation(updateCompilationRequest, compId);
        log.info("Request Update received to update user, updateUser: " + updateCompilation);
        return updateCompilation;
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest,
                                    @PathVariable Long eventId) {
        EventFullDto eventFullDtoUpdate = eventService.updateEventAdmin(updateEventAdminRequest, eventId);
        log.info("Request Update received to update event whit id = : " + eventId);
        return eventFullDtoUpdate;
    }

    @DeleteMapping("/events/{eventId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        log.info("Request Delete received to delete event whit id = : " + eventId);
    }

    @GetMapping("/events")
    public List<EventFullDto> getEventsWhitFilters(@RequestParam(required = false) List<Long> users,
                                       @RequestParam(required = false) List<State> states,
                                       @RequestParam(required = false) List<Long> categories,
                                       @RequestParam(required = false) String rangeStart,
                                       @RequestParam(required = false) String rangeEnd,
                                       @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                       @Positive @RequestParam(defaultValue = "10") Integer size) {

        log.debug("Request Get received whit parameters: " +
                        "userIds: {}, states: {}, categoryIds: {}, rangeStart: {}, rangeEnd {}, from: {}, size: {}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.adminFindEventsWhitFilter(users, states, categories, rangeStart,
                rangeEnd, from, size);
    }
}
