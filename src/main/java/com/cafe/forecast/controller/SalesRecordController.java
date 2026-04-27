package com.cafe.forecast.controller;

import com.cafe.forecast.dto.ApiResponse;
import com.cafe.forecast.dto.SalesRecordDTO;
import com.cafe.forecast.service.SalesRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
@Tag(name = "Registros de Vendas", description = "Histórico de vendas para análise e previsão de demanda")
public class SalesRecordController {

    private final SalesRecordService salesRecordService;

    @GetMapping
    @Operation(summary = "Listar todos os registros de venda")
    public ResponseEntity<ApiResponse<List<SalesRecordDTO>>> findAll() {
        List<SalesRecordDTO> records = salesRecordService.findAll();
        return ResponseEntity.ok(ApiResponse.ok(records, "Registros de venda listados com sucesso"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar registro por ID")
    public ResponseEntity<ApiResponse<SalesRecordDTO>> findById(@PathVariable Long id) {
        SalesRecordDTO record = salesRecordService.findById(id);
        return ResponseEntity.ok(ApiResponse.ok(record, "Registro encontrado"));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Buscar vendas por produto")
    public ResponseEntity<ApiResponse<List<SalesRecordDTO>>> findByProduct(@PathVariable Long productId) {
        List<SalesRecordDTO> records = salesRecordService.findByProduct(productId);
        return ResponseEntity.ok(ApiResponse.ok(records, "Vendas do produto listadas com sucesso"));
    }

    @GetMapping("/period")
    @Operation(summary = "Buscar vendas por período")
    public ResponseEntity<ApiResponse<List<SalesRecordDTO>>> findByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<SalesRecordDTO> records = salesRecordService.findByPeriod(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.ok(records,
                String.format("Vendas entre %s e %s listadas com sucesso", startDate, endDate)));
    }

    @GetMapping("/product/{productId}/period")
    @Operation(summary = "Buscar vendas de um produto em um período")
    public ResponseEntity<ApiResponse<List<SalesRecordDTO>>> findByProductAndPeriod(
            @PathVariable Long productId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<SalesRecordDTO> records = salesRecordService.findByProductAndPeriod(productId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.ok(records, "Vendas listadas com sucesso"));
    }

    @GetMapping("/day/{dayOfWeek}")
    @Operation(summary = "Buscar vendas por dia da semana (ex: MONDAY, FRIDAY)")
    public ResponseEntity<ApiResponse<List<SalesRecordDTO>>> findByDayOfWeek(@PathVariable String dayOfWeek) {
        List<SalesRecordDTO> records = salesRecordService.findByDayOfWeek(dayOfWeek);
        return ResponseEntity.ok(ApiResponse.ok(records,
                String.format("Vendas de %s listadas com sucesso", dayOfWeek)));
    }

    @GetMapping("/product/{productId}/total")
    @Operation(summary = "Total de unidades vendidas de um produto")
    public ResponseEntity<ApiResponse<Integer>> getTotalSold(@PathVariable Long productId) {
        Integer total = salesRecordService.getTotalSoldByProduct(productId);
        return ResponseEntity.ok(ApiResponse.ok(total, "Total calculado com sucesso"));
    }

    @PostMapping
    @Operation(summary = "Registrar nova venda")
    public ResponseEntity<ApiResponse<SalesRecordDTO>> save(@Valid @RequestBody SalesRecordDTO salesRecordDTO) {
        SalesRecordDTO created = salesRecordService.save(salesRecordDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(created, "Venda registrada com sucesso"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar registro de venda")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        salesRecordService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.success(null, "Registro removido com sucesso"));
    }
}
