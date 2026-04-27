package com.cafe.forecast.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ValidationException extends RuntimeException {
    private final HttpStatus status = HttpStatus.BAD_REQUEST;
    private final String errorCode = "VALIDATION_ERROR";

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
