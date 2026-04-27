package com.cafe.forecast.controller;

import com.cafe.forecast.dto.ApiResponse;
import com.cafe.forecast.integration.mogo.MogoSalesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/integration")
@RequiredArgsConstructor
@Tag(name = "Integrações", description = "Endpoints de sincronização com sistemas externos")
public class IntegrationController {

    private final MogoSalesService mogoSalesService;

    @PostMapping("/mogo/sync")
    @Operation(summary = "Sincronizar vendas do Mogo (sob demanda)")
    public ResponseEntity<ApiResponse<?>> syncMogoSales(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        if (startDate == null) startDate = LocalDate.now();
        if (endDate == null) endDate = LocalDate.now();

        mogoSalesService.syncSalesByPeriod(startDate, endDate);

        return ResponseEntity.ok(
                ApiResponse.ok(null, "Sincronização iniciada com sucesso")
        );
    }

    @GetMapping("/mogo/stats")
    @Operation(summary = "Estatísticas de sincronização")
    public ResponseEntity<ApiResponse<MogoSalesService.SyncStats>> getSyncStats() {
        return ResponseEntity.ok(
                ApiResponse.ok(mogoSalesService.getSyncStats(), "Estatísticas recuperadas")
        );
    }
}
