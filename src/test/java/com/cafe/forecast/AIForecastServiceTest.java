package com.cafe.forecast.service;

import com.cafe.forecast.model.Product;
import com.cafe.forecast.model.SalesRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AIForecastService Tests")
class AIForecastServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AIForecastService aiForecastService;

    private Product product;
    private SalesRecord salesRecord;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(aiForecastService, "apiKey", "test-key");
        ReflectionTestUtils.setField(aiForecastService, "apiUrl", "https://api.groq.com/test");
        ReflectionTestUtils.setField(aiForecastService, "model", "llama-3.3-70b-versatile");

        product = new Product();
        product.setId(1L);
        product.setName("Cappuccino");
        product.setPrice(BigDecimal.valueOf(8.50));

        salesRecord = new SalesRecord();
        salesRecord.setProduct(product);
        salesRecord.setSaleDate(LocalDate.now());
        salesRecord.setQuantitySold(5);
        salesRecord.setDayOfWeek("MONDAY");
    }

    @Test
    @DisplayName("Deve gerar previsão de demanda com sucesso")
    void testForecastDemandSuccess() {
        Map<String, Object> mockResponse = Map.of(
                "choices", List.of(Map.of(
                        "message", Map.of("content", "Previsão: demanda de 20 unidades amanhã")
                ))
        );

        ResponseEntity<Map> response = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Map.class)))
                .thenReturn(response);

        String result = aiForecastService.forecastDemand("Cappuccino", List.of(salesRecord));

        assertThat(result)
                .isNotBlank()
                .contains("Previsão");

        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(), eq(Map.class));
    }

    @Test
    @DisplayName("Deve lançar AIForecastException quando RestTemplate falha")
    void testForecastDemandRestClientException() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Map.class)))
                .thenThrow(new RestClientException("Connection error"));

        assertThatThrownBy(() -> aiForecastService.forecastDemand("Cappuccino", List.of(salesRecord)))
                .isInstanceOf(AIForecastException.class)
                .hasMessageContaining("Erro ao chamar Groq API");
    }

    @Test
    @DisplayName("Deve lançar AIForecastException quando resposta é null")
    void testForecastDemandNullResponse() {
        ResponseEntity<Map> response = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Map.class)))
                .thenReturn(response);

        assertThatThrownBy(() -> aiForecastService.forecastDemand("Cappuccino", List.of(salesRecord)))
                .isInstanceOf(AIForecastException.class)
                .hasMessageContaining("Resposta vazia");
    }

    @Test
    @DisplayName("Deve lançar AIForecastException quando choices está vazio")
    void testForecastDemandEmptyChoices() {
        Map<String, Object> mockResponse = Map.of("choices", List.of());
        ResponseEntity<Map> response = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Map.class)))
                .thenReturn(response);

        assertThatThrownBy(() -> aiForecastService.forecastDemand("Cappuccino", List.of(salesRecord)))
                .isInstanceOf(AIForecastException.class)
                .hasMessageContaining("Nenhuma resposta recebida");
    }

    @Test
    @DisplayName("Deve gerar sugestão de produção com sucesso")
    void testSuggestProductionSuccess() {
        Map<String, Object> mockResponse = Map.of(
                "choices", List.of(Map.of(
                        "message", Map.of("content", "Sugestão: produzir 25 unidades hoje")
                ))
        );

        ResponseEntity<Map> response = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Map.class)))
                .thenReturn(response);

        String result = aiForecastService.suggestProduction("Cappuccino", 20, 5);

        assertThat(result)
                .isNotBlank()
                .contains("Sugestão");

        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(), eq(Map.class));
    }

    @Test
    @DisplayName("Deve lançar AIForecastException ao sugerir produção com erro de API")
    void testSuggestProductionApiError() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Map.class)))
                .thenThrow(new RestClientException("API error"));

        assertThatThrownBy(() -> aiForecastService.suggestProduction("Cappuccino", 20, 5))
                .isInstanceOf(AIForecastException.class)
                .hasMessageContaining("Erro ao chamar Groq API");
    }

    @Test
    @DisplayName("Deve lidar com múltiplos registros de vendas na previsão")
    void testForecastDemandWithMultipleRecords() {
        SalesRecord record2 = new SalesRecord();
        record2.setProduct(product);
        record2.setSaleDate(LocalDate.now().minusDays(1));
        record2.setQuantitySold(7);
        record2.setDayOfWeek("SUNDAY");

        Map<String, Object> mockResponse = Map.of(
                "choices", List.of(Map.of(
                        "message", Map.of("content", "Análise com múltiplos dias")
                ))
        );

        ResponseEntity<Map> response = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Map.class)))
                .thenReturn(response);

        String result = aiForecastService.forecastDemand("Cappuccino", List.of(salesRecord, record2));

        assertThat(result).isNotBlank();
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(), eq(Map.class));
    }
}
