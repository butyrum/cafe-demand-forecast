package com.cafe.forecast.dto.mogo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MogoSalesResponse {

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("data")
    private List<SaleData> data;

    @JsonProperty("message")
    private String message;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SaleData {

        @JsonProperty("id")
        private String id;

        @JsonProperty("product_id")
        private String productId;

        @JsonProperty("product_name")
        private String productName;

        @JsonProperty("quantity")
        private Integer quantity;

        @JsonProperty("total")
        private Double total;

        @JsonProperty("sale_date")
        private LocalDate saleDate;

        @JsonProperty("payment_method")
        private String paymentMethod;

        @JsonProperty("status")
        private String status;
    }
}
