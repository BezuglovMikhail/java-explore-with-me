package ru.practicum.stats.hendler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    @JsonInclude(NON_NULL)
    private List<String> errors;

    private String message;

    private String reason;

    @Enumerated(EnumType.STRING)
    private HttpStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS")
    private LocalDateTime timestamp;

    private String mapping;

    @JsonInclude(NON_NULL)
    private String details;

    @JsonInclude(NON_NULL)
    private List<String> trace;
}