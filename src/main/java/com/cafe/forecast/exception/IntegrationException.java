package com.cafe.forecast.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class IntegrationException extends RuntimeException {
    private final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    private final String errorCode = "INTEGRATION_ERROR";

    public IntegrationException(String message) {
        super(message);
    }

    public IntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
