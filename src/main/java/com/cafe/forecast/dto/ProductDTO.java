package com.cafe.forecast.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long id;

    @NotBlank(message = "Nome do produto é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String name;

    @NotBlank(message = "Categoria é obrigatória")
    @Size(min = 2, max = 50, message = "Categoria deve ter entre 2 e 50 caracteres")
    private String category;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    @DecimalMax(value = "99999.99", message = "Preço não pode exceder 99999.99")
    private BigDecimal price;

    @NotBlank(message = "Unidade de medida é obrigatória")
    @Size(min = 2, max = 20, message = "Unidade deve ter entre 2 e 20 caracteres")
    private String unitOfMeasure;

    private Boolean isActive = true;
}
