package ru.practicum.ewm.exeption;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.model.ApiError;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handlerValidationException(final ValidationException e) {
        return new ApiError(e.getClass().getName(), e.getMessage(), e.getReason(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handlerNotFoundException(final NotFoundException e) {
        return new ApiError(e.getClass().getName(), e.getMessage(), e.getLocalizedMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handlerThrowable(final Throwable e) {
        e.printStackTrace();
        return new ApiError(e.getClass().getName(), e.getMessage(), e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlerIncorrectParameterException(final IncorrectParameterException e) {
        return new ApiError(e.getClass().getName(), e.getMessage(),
                e.getLocalizedMessage(), HttpStatus.CONFLICT, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handlerConstraintViolationException(final ConstraintViolationException e) {
        e.printStackTrace();
        return new ApiError(e.getClass().getName(), e.getMessage(), e.getLocalizedMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handlerMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        e.printStackTrace();
        return new ApiError(e.getClass().getName(), e.getMessage(), e.getLocalizedMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlerDataIntegrityViolationException(final DataIntegrityViolationException e) {
        e.printStackTrace();
        return new ApiError(e.getClass().getName(), e.getMessage(), e.getLocalizedMessage(), HttpStatus.CONFLICT, LocalDateTime.now());
    }
}
