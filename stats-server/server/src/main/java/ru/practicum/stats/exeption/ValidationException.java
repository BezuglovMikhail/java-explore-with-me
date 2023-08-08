package ru.practicum.stats.exeption;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Slf4j
@Data
public class ValidationException extends RuntimeException {

    private String error;
    private String message;
    private String reason;

    @Enumerated(EnumType.STRING)
    private HttpStatus status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS")
    private LocalDateTime timestamp;

    public ValidationException(String error, String message, String reason, HttpStatus status, LocalDateTime timestamp) {
        this.error = error;
        this.message = message;
        this.reason = reason;
        this.status = status;
        this.timestamp = timestamp;
        log.error(message);
    }
}