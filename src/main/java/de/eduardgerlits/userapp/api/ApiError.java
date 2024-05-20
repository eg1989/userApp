package de.eduardgerlits.userapp.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ApiError {

    private final LocalDateTime timestamp;
    private final HttpStatus status;
    private final String message;

    public ApiError(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

}
