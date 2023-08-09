package ru.practicum.ewm.exeption;

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
public class ApiError {

    @JsonInclude(NON_NULL)
    List<String> errors;

    private String message;

    private String reason;

    @Enumerated(EnumType.STRING)
    private HttpStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS")
    private LocalDateTime timestamp;

    String mapping;

    @JsonInclude(NON_NULL)
    String details;

    @JsonInclude(NON_NULL)
    List<String> trace;
}
