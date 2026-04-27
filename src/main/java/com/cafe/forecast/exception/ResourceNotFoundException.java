package com.cafe.forecast.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final HttpStatus status = HttpStatus.NOT_FOUND;
    private final String errorCode = "RESOURCE_NOT_FOUND";

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
