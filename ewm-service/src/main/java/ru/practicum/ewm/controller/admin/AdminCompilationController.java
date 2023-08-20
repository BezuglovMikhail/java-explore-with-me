package ru.practicum.ewm.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.newdto.NewCompilationDto;
import ru.practicum.ewm.request.UpdateCompilationRequest;
import ru.practicum.ewm.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin")
@Slf4j
public class AdminCompilationController {

    @Autowired
    private CompilationService compilationService;

    @PostMapping("/compilations")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        CompilationDto addedCompilation = compilationService.save(newCompilationDto);
        log.info("Request Post received to add compilation: {}", addedCompilation);
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
        log.info("Request Update received to update user, updateUser: {}", updateCompilation);
        return updateCompilation;
    }
}
