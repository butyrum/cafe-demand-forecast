package com.cafe.forecast.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesRecordDTO {

    private Long id;

    @NotNull(message = "ID do produto é obrigatório")
    private Long productId;

    private String productName;

    @NotNull(message = "Data de venda é obrigatória")
    @PastOrPresent(message = "Data de venda não pode ser no futuro")
    private LocalDate saleDate;

    @NotNull(message = "Quantidade vendida é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser pelo menos 1")
    @Max(value = 10000, message = "Quantidade não pode exceder 10000")
    private Integer quantitySold;

    private String dayOfWeek;
}
