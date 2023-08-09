package ru.practicum.stats.hendler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import ru.practicum.stats.exeption.ValidationException;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    public static final String UNEXPECTED_ERROR = "Unexpected error";

    public static final String INCORRECTLY_Argument = "Incorrectly argument.";

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handlerIllegalArgumentException(IllegalArgumentException e, WebRequest request) {
        return makeApiError(INCORRECTLY_Argument, e, BAD_REQUEST, request);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handlerValidationException(ValidationException e, WebRequest request) {
        return makeApiError(e.getReason(), e, BAD_REQUEST, request);
    }

    private ResponseEntity<Object> makeApiError(String reason, Throwable ex, HttpStatus status, WebRequest request) {
        log.error("{}: {}", reason, ex.getMessage());
        ex.printStackTrace();
        ErrorResponse errorResponse = makeBody(reason, status, request, ex);
        return new ResponseEntity<>(errorResponse, status);
    }

    private ErrorResponse makeBody(String reason, HttpStatus status, WebRequest request, Throwable ex) {
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

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMapping(getRequestURI(request));
        errorResponse.setStatus(status);
        errorResponse.setReason(reason);
        errorResponse.setMessage(message);
        errorResponse.setDetails(details);
        errorResponse.setErrors(errors.size() > 1 ? errors : null);
        errorResponse.setTrace(stackTrace);

        return errorResponse;
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
