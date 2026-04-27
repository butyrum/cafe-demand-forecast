package com.cafe.forecast.config;

import com.cafe.forecast.dto.ApiResponse;
import com.cafe.forecast.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
    }

    @Test
    @DisplayName("Deve tratar ResourceNotFoundException corretamente")
    void testHandleResourceNotFound() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Produto não encontrado");

        ResponseEntity<ApiResponse<?>> response = globalExceptionHandler.handleResourceNotFound(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).contains("Produto não encontrado");
        assertThat(response.getBody().getStatusCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("Deve tratar ValidationException corretamente")
    void testHandleValidationException() {
        ValidationException exception = new ValidationException("Dados inválidos");

        ResponseEntity<ApiResponse<?>> response = globalExceptionHandler.handleValidationException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).contains("Dados inválidos");
        assertThat(response.getBody().getStatusCode()).isEqualTo(400);
    }

    @Test
    @DisplayName("Deve tratar AIForecastException corretamente")
    void testHandleAIForecastException() {
        AIForecastException exception = new AIForecastException("Erro na chamada da API");

        ResponseEntity<ApiResponse<?>> response = globalExceptionHandler.handleAIForecastException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getError()).isEqualTo("AI_FORECAST_ERROR");
        assertThat(response.getBody().getStatusCode()).isEqualTo(500);
    }

    @Test
    @DisplayName("Deve tratar IllegalArgumentException corretamente")
    void testHandleIllegalArgument() {
        IllegalArgumentException exception = new IllegalArgumentException("Argumento inválido");

        ResponseEntity<ApiResponse<?>> response = globalExceptionHandler.handleIllegalArgument(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).contains("Argumento inválido");
        assertThat(response.getBody().getStatusCode()).isEqualTo(400);
    }

    @Test
    @DisplayName("Deve tratar Exception genérica corretamente")
    void testHandleGlobalException() {
        Exception exception = new Exception("Erro não tratado");

        ResponseEntity<ApiResponse<?>> response = globalExceptionHandler.handleGlobalException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).contains("Erro interno do servidor");
        assertThat(response.getBody().getStatusCode()).isEqualTo(500);
    }

    @Test
    @DisplayName("Deve incluir timestamp em todas respostas")
    void testResponseIncludesTimestamp() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Teste");

        ResponseEntity<ApiResponse<?>> response = globalExceptionHandler.handleResourceNotFound(exception, webRequest);

        assertThat(response.getBody().getTimestamp()).isNotNull();
    }

    @Test
    @DisplayName("Deve ter success=false em todas exceções")
    void testResponseHasSuccessFalse() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Teste");

        ResponseEntity<ApiResponse<?>> response = globalExceptionHandler.handleResourceNotFound(exception, webRequest);

        assertThat(response.getBody().isSuccess()).isFalse();
    }
}
