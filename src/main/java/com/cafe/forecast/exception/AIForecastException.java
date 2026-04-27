package com.cafe.forecast.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AIForecastException extends RuntimeException {
    private final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    private final String errorCode = "AI_FORECAST_ERROR";

    public AIForecastException(String message) {
        super(message);
    }

    public AIForecastException(String message, Throwable cause) {
        super(message, cause);
    }
}
