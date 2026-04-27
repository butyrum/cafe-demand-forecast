package com.cafe.forecast.integration.mogo;

import com.cafe.forecast.dto.mogo.MogoSalesResponse;
import com.cafe.forecast.dto.mogo.MogoSalesResponse.SaleData;
import com.cafe.forecast.model.SalesRecord;
import com.cafe.forecast.repository.SalesRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Service
@Slf4j
@RequiredArgsConstructor
public class MogoSalesService {

    private final MogoClient mogoClient;
    private final SalesRecordRepository salesRecordRepository;

    /**
     * Sincroniza vendas do Mogo diariamente (agenda às 23:00)
     */
    @Scheduled(cron = "0 0 23 * * *")
    public void syncDailySales() {
        log.info("🔄 Iniciando sincronização diária com Mogo Gourmet");

        LocalDate today = LocalDate.now();

        mogoClient.getSalesByPeriod(today, today)
                .subscribe(
                        response -> {
                            if (response.getData() != null) {
                                processSales(response.getData());
                            }
                        },
                        error -> log.error("❌ Falha na sincronização: {}", error.getMessage())
                );
    }

    /**
     * Sincroniza vendas de um período específico (sob demanda)
     */
    public void syncSalesByPeriod(LocalDate startDate, LocalDate endDate) {
        log.info("🔄 Sincronizando vendas de {} até {}", startDate, endDate);

        mogoClient.getSalesByPeriod(startDate, endDate)
                .subscribe(
                        response -> {
                            if (response.getData() != null) {
                                processSales(response.getData());
                            }
                        },
                        error -> log.error("❌ Falha na sincronização: {}", error.getMessage())
                );
    }

    /**
     * Processa vendas e salva no banco local
     */
    private void processSales(List<SaleData> sales) {
        int saved = 0;
        int skipped = 0;

        for (SaleData sale : sales) {
            // Verifica se já existe (evita duplicidade)
            if (salesRecordRepository.existsByMogoId(sale.getId())) {
                log.debug("Venda já existe, pulando: {}", sale.getId());
                skipped++;
                continue;
            }

            try {
                SalesRecord record = convertToEntity(sale);
                salesRecordRepository.save(record);
                saved++;
                log.info("✅ Venda sincronizada: {} | Produto: {} | Qtd: {}",
                        sale.getId(), sale.getProductName(), sale.getQuantity());
            } catch (Exception e) {
                log.error("❌ Erro ao salvar venda {}: {}", sale.getId(), e.getMessage());
            }
        }

        log.info("📊 Sincronização concluída: {} salvas, {} puladas, {} total",
                saved, skipped, sales.size());
    }

    /**
     * Converte DTO do Mogo para entidade local
     */
    private SalesRecord convertToEntity(SaleData sale) {
        return SalesRecord.builder()
                .mogoId(sale.getId())
                .mogoProductId(sale.getProductId())
                .saleDate(sale.getSaleDate())
                .quantitySold(sale.getQuantity())
                .totalValue(java.math.BigDecimal.valueOf(sale.getTotal()))
                .dayOfWeek(DayOfWeek.from(sale.getSaleDate()).toString())
                .syncedFromMogo(true)
                .paymentMethod(sale.getPaymentMethod())
                .status(sale.getStatus())
                .build();
    }

    /**
     * Busca vendas recentes do Mogo (sob demanda, não agendado)
     */
    public Mono<List<SaleData>> getRecentSales(LocalDate date) {
        return mogoClient.getSalesByPeriod(date, date)
                .map(response -> response.getData() != null ? response.getData() : List.of());
    }

    /**
     * Retorna estatísticas de sincronização
     */
    public SyncStats getSyncStats() {
        long totalSynced = salesRecordRepository.countBySyncedFromMogoTrue();
        LocalDate today = LocalDate.now();
        long todaySynced = salesRecordRepository.countBySaleDateAndSyncedFromMogoTrue(today);

        return SyncStats.builder()
                .totalSynced(totalSynced)
                .todaySynced(todaySynced)
                .lastSync(LocalDate.now())
                .build();
    }

    /**
     * DTO para estatísticas de sincronização
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SyncStats {
        private long totalSynced;
        private long todaySynced;
        private LocalDate lastSync;
    }
}
