package com.cafe.forecast.integration.mogo;

import com.cafe.forecast.dto.mogo.MogoSalesResponse;
import com.cafe.forecast.dto.mogo.MogoProductDTO;
import com.cafe.forecast.exception.IntegrationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class MogoClient {

    private final WebClient mogoWebClient;

    /**
     * Busca vendas do Mogo Gourmet por período
     */
    public Mono<MogoSalesResponse> getSalesByPeriod(LocalDate startDate, LocalDate endDate) {
        log.info("Buscando vendas no Mogo de {} até {}", startDate, endDate);

        return mogoWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/sales")
                        .queryParam("start_date", startDate.toString())
                        .queryParam("end_date", endDate.toString())
                        .build())
                .header("Authorization", "Bearer ${mogo.api.token}")
                .header("Content-Type", "application/json")
                .retrieve()
                .bodyToMono(MogoSalesResponse.class)
                .doOnSuccess(response ->
                        log.info("Vendas recuperadas: {} registros",
                                response.getData() != null ? response.getData().size() : 0))
                .doOnError(error ->
                        log.error("Erro ao buscar vendas no Mogo: {}", error.getMessage()))
                .onErrorResume(error -> {
                    log.error("Falha na integração com Mogo: {}", error.getMessage());
                    return Mono.error(new IntegrationException(
                            "Erro ao sincronizar com Mogo Gourmet: " + error.getMessage(),
                            error
                    ));
                });
    }

    /**
     * Busca vendas de um produto específico
     */
    public Mono<MogoSalesResponse> getSalesByProduct(String productId, LocalDate date) {
        log.info("Buscando vendas do produto {} no Mogo", productId);

        return mogoWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/sales/product/{productId}")
                        .queryParam("date", date.toString())
                        .build(productId))
                .header("Authorization", "Bearer ${mogo.api.token}")
                .header("Content-Type", "application/json")
                .retrieve()
                .bodyToMono(MogoSalesResponse.class)
                .onErrorResume(error -> {
                    log.error("Falha ao buscar vendas do produto: {}", error.getMessage());
                    return Mono.error(new IntegrationException(
                            "Erro ao buscar vendas do produto: " + error.getMessage(),
                            error
                    ));
                });
    }

    /**
     * Busca todos os produtos do Mogo
     */
    public Mono<List<MogoProductDTO>> getProducts() {
        log.info("Buscando produtos no Mogo");

        return mogoWebClient.get()
                .uri("/api/v1/products")
                .header("Authorization", "Bearer ${mogo.api.token}")
                .header("Content-Type", "application/json")
                .retrieve()
                .bodyToMono(MogoProductDTO[].class)
                .map(List::of)
                .onErrorResume(error -> {
                    log.error("Falha ao buscar produtos: {}", error.getMessage());
                    return Mono.error(new IntegrationException(
                            "Erro ao buscar produtos do Mogo: " + error.getMessage(),
                            error
                    ));
                });
    }
}
