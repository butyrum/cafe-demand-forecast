package com.cafe.forecast.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String error;
    private Integer statusCode;
    private LocalDateTime timestamp;

    // Factory methods para facilitar o uso nos controllers
    public static <T> ApiResponse<T> ok(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .statusCode(200)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> created(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .statusCode(201)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .statusCode(200)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .error("RESOURCE_NOT_FOUND")
                .message(message)
                .statusCode(404)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> badRequest(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .error("BAD_REQUEST")
                .message(message)
                .statusCode(400)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String errorCode, String message, int statusCode) {
        return ApiResponse.<T>builder()
                .success(false)
                .error(errorCode)
                .message(message)
                .statusCode(statusCode)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> internalServerError(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .error("INTERNAL_SERVER_ERROR")
                .message(message)
                .statusCode(500)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
