package ru.practicum.ewm.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private String error;
    private String message;
    private String reason;

    @Enumerated(EnumType.STRING)
    private HttpStatus status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS")
    private LocalDateTime timestamp;
}
