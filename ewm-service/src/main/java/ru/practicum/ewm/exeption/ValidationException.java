package ru.practicum.ewm.exeption;

import lombok.Getter;

public class ValidationException extends RuntimeException {

    public static final String REASON = "Object failed validation";

    @Getter
    private final String reason;

    public ValidationException(String reason, String message) {
        super(message);
        this.reason = reason;
    }

    public ValidationException(String message) {
        this(REASON, message);
    }
}
