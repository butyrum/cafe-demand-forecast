package com.cafe.forecast.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductionSuggestionRequest {

    @NotBlank(message = "Nome do produto é obrigatório")
    private String productName;

    @NotNull(message = "Demanda prevista é obrigatória")
    @Min(value = 1, message = "Demanda deve ser pelo menos 1")
    @Max(value = 50000, message = "Demanda não pode exceder 50000")
    private Integer forecastedDemand;

    @NotNull(message = "Estoque atual é obrigatório")
    @Min(value = 0, message = "Estoque não pode ser negativo")
    @Max(value = 50000, message = "Estoque não pode exceder 50000")
    private Integer currentStock;
}
