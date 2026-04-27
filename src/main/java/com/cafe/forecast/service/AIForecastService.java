package com.cafe.forecast.service;

import com.cafe.forecast.exception.AIForecastException; // ✅ IMPORT ADICIONADO
import com.cafe.forecast.model.SalesRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIForecastService {

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    @Value("${groq.api.model}")
    private String model;

    private final RestTemplate restTemplate;

    public String forecastDemand(String productName, List<SalesRecord> salesHistory) {
        log.info("Iniciando previsão de demanda para: {}", productName);

        StringBuilder historyText = new StringBuilder();
        for (SalesRecord record : salesHistory) {
            historyText.append(String.format(
                    "- Data: %s | Dia: %s | Quantidade vendida: %d\n",
                    record.getSaleDate(),
                    record.getDayOfWeek(),
                    record.getQuantitySold()
            ));
        }

        String prompt = String.format("""
            Você é um assistente especializado em gestão de cafeterias.
            
            Com base no histórico de vendas abaixo do produto "%s", faça:
            1. Uma previsão de demanda para os próximos 7 dias
            2. Sugestão de quantidade a produzir por dia
            3. Identifique padrões por dia da semana
            4. Dê uma recomendação objetiva para o gestor
            
            Histórico de vendas:
            %s
            
            Responda em português, de forma clara e objetiva.
            """, productName, historyText.toString());

        log.debug("Enviando prompt para Groq API: {}", prompt);
        String forecast = callGroqAPI(prompt);
        log.info("Previsão de demanda gerada com sucesso para: {}", productName);
        return forecast;
    }

    public String suggestProduction(String productName, int forecastedDemand, int currentStock) {
        log.info("Gerando sugestão de produção para: {} (Demanda: {}, Estoque: {})",
                productName, forecastedDemand, currentStock);

        String prompt = String.format("""
            Você é um assistente especializado em gestão de cafeterias.
            
            Produto: %s
            Demanda prevista para amanhã: %d unidades
            Estoque atual: %d unidades
            
            Com base nisso:
            1. Quanto devo produzir hoje?
            2. Preciso comprar algum ingrediente?
            3. Alguma recomendação adicional?
            
            Responda em português, de forma direta e objetiva.
            """, productName, forecastedDemand, currentStock);

        log.debug("Enviando prompt para Groq API: {}", prompt);
        String suggestion = callGroqAPI(prompt);
        log.info("Sugestão de produção gerada com sucesso para: {}", productName);
        return suggestion;
    }

    private String callGroqAPI(String prompt) {
        try {
            Map<String, Object> requestBody = Map.of(
                    "model", model,
                    "messages", List.of(
                            Map.of("role", "user", "content", prompt)
                    ),
                    "temperature", 0.3
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            log.debug("Chamando Groq API em: {}", apiUrl);
            ResponseEntity<Map> response = restTemplate.exchange(
                    apiUrl, HttpMethod.POST, request, Map.class
            );

            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null) {
                throw new AIForecastException("Resposta vazia da API Groq");
            }

            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            if (choices == null || choices.isEmpty()) {
                throw new AIForecastException("Nenhuma resposta recebida da API Groq");
            }

            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            String content = (String) message.get("content");

            log.debug("Resposta recebida da Groq API com sucesso");
            return content;

        } catch (RestClientException ex) {
            log.error("Erro ao chamar Groq API: {}", ex.getMessage(), ex);
            throw new AIForecastException("Erro ao chamar Groq API: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error("Erro inesperado ao processar resposta da IA: {}", ex.getMessage(), ex);
            throw new AIForecastException("Erro ao processar resposta da IA: " + ex.getMessage(), ex);
        }
    }
}
