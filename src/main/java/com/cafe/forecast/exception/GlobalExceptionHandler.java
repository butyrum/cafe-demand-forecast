package com.cafe.forecast.exception;

import com.cafe.forecast.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFound(
            ResourceNotFoundException ex,
            WebRequest request) {
        log.warn("Resource not found: {}", ex.getMessage());
        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .error(ex.getErrorCode())
                .message(ex.getMessage())
                .statusCode(ex.getStatus().value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(
            ValidationException ex,
            WebRequest request) {
        log.warn("Validation error: {}", ex.getMessage());
        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .error(ex.getErrorCode())
                .message(ex.getMessage())
                .statusCode(ex.getStatus().value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(AIForecastException.class)
    public ResponseEntity<ApiResponse<?>> handleAIForecastException(
            AIForecastException ex,
            WebRequest request) {
        log.error("AI Forecast error: {}", ex.getMessage(), ex);
        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .error(ex.getErrorCode())
                .message(ex.getMessage())
                .statusCode(ex.getStatus().value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationErrors(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        log.warn("Validation failed: {}", errors);

        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .error("VALIDATION_FIELD_ERROR")
                .message("Validation failed")
                .data(errors)
                .statusCode(400)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgument(
            IllegalArgumentException ex,
            WebRequest request) {
        log.warn("Illegal argument: {}", ex.getMessage());
        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .error("ILLEGAL_ARGUMENT")
                .message(ex.getMessage())
                .statusCode(400)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGlobalException(
            Exception ex,
            WebRequest request) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .error("INTERNAL_SERVER_ERROR")
                .message("Erro interno do servidor. Verifique os logs.")
                .statusCode(500)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(IntegrationException.class)
    public ResponseEntity<ApiResponse<?>> handleIntegrationException(
            IntegrationException ex,
            WebRequest request) {
        log.error("Integration error: {}", ex.getMessage(), ex);
        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .error(ex.getErrorCode())
                .message(ex.getMessage())
                .statusCode(ex.getStatus().value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, ex.getStatus());
    }

}
