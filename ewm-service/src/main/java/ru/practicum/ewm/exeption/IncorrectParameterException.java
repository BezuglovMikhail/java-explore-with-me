package ru.practicum.ewm.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(HttpStatus.CONFLICT)
public class IncorrectParameterException extends RuntimeException {

    private static final String DEFAULT_REASON = "For the requested operation the conditions are not met.";
    public final String reason;

    public IncorrectParameterException(String reason, String message) {
        super(message);
        this.reason = reason;
    }

    public IncorrectParameterException(String message) {
        this(DEFAULT_REASON, message);
    }

    public String getReason() {
        return reason;
    }
}
