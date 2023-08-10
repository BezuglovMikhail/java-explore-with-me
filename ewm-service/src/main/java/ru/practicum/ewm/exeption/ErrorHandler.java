package ru.practicum.ewm.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    private static final String UNEXPECTED_ERROR = "Unexpected error";
    private static final String INTEGRITY_CONSTRAINT_HAS_BEEN_VIOLATED = "Integrity constraint has been violated.";
    private static final String INCORRECTLY_MADE_REQUEST = "Incorrectly made request.";
    private static final String CONSTRAIN_EXCEPTION = "Constraint exception";


    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handlerValidationException(ValidationException e, WebRequest request) {
        return makeApiError(e.getReason(), e, BAD_REQUEST, request);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handlerNotFoundException(NotFoundException e, WebRequest request) {
        return makeApiError(e.getReason(), e, NOT_FOUND, request);
    }

    @ExceptionHandler()
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handlerThrowable(Throwable e, WebRequest request) {
        return makeApiError(UNEXPECTED_ERROR, e, INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(IncorrectParameterException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handlerIncorrectParameterException(IncorrectParameterException e, WebRequest request) {
        return makeApiError(e.getReason(), e, CONFLICT, request);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handlerConstraintViolationException(ConstraintViolationException e, WebRequest request) {
        return makeApiError(CONSTRAIN_EXCEPTION, e, BAD_REQUEST, request);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request) {
        return makeApiError(INCORRECTLY_MADE_REQUEST, e, BAD_REQUEST, request);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handlerDataIntegrityViolationException(DataIntegrityViolationException e, WebRequest request) {
        return makeApiError(INTEGRITY_CONSTRAINT_HAS_BEEN_VIOLATED, e, CONFLICT, request);
    }

    private ResponseEntity<Object> makeApiError(String reason, Throwable ex, HttpStatus status, WebRequest request) {
        log.error("{}: {}", reason, ex.getMessage());
        ex.printStackTrace();
        ApiError apiError = makeBody(reason, status, request, ex);
        return new ResponseEntity<>(apiError, status);
    }

    private ApiError makeBody(String reason, HttpStatus status, WebRequest request, Throwable ex) {
        List<String> errors;
        if (ex instanceof BindException) {
            errors = ((BindException) ex)
                    .getAllErrors()
                    .stream()
                    .map(this::getErrorString)
                    .collect(Collectors.toCollection(LinkedList::new));
        } else {
            errors = Arrays.stream(ex.getMessage().split(", ")).collect(Collectors.toList());
        }

        List<String> stackTrace = UNEXPECTED_ERROR.equals(reason)
                ? Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList())
                : null;
        String message = !errors.isEmpty() ? errors.get(0) : "No message";
        String details = !errors.isEmpty() && !Objects.equals(ex.getMessage(), errors.get(0)) ? ex.getMessage() : null;

        ApiError apiError = new ApiError();
        apiError.setMapping(getRequestURI(request));
        apiError.setStatus(status);
        apiError.setReason(reason);
        apiError.setMessage(message);
        apiError.setDetails(details);
        apiError.setErrors(errors.size() > 1 ? errors : null);
        apiError.setTrace(stackTrace);

        return apiError;
    }

    private String getErrorString(ObjectError error) {
        if (error instanceof FieldError) {
            FieldError fieldError = ((FieldError) error);
            String fieldName = fieldError.getField();
            String defMassage = fieldError.getDefaultMessage();
            String rejectedValue = fieldError.getRejectedValue() != null && !"".equals(fieldError.getRejectedValue())
                    ? fieldError.getRejectedValue().toString()
                    : "null";
            return String.format("Field: %s. Error: %s. Value: %s", fieldName, defMassage, rejectedValue);
        }
        return error.getDefaultMessage();
    }

    private String getRequestURI(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            HttpServletRequest requestHttp = ((ServletWebRequest) request).getRequest();
            return String.format("%s %s", requestHttp.getMethod(), requestHttp.getRequestURI());
        } else {
            return "";
        }
    }
}
