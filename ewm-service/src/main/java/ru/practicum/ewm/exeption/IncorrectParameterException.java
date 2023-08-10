package ru.practicum.ewm.exeption;

public class IncorrectParameterException extends RuntimeException {

    private static final String DEFAULT_REASON = "For the requested operation the conditions are not met.";
    private final String reason;

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
