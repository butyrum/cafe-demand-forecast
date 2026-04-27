package com.cafe.forecast.dto.mogo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MogoProductDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("category")
    private String category;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("active")
    private Boolean active;

    @JsonProperty("sku")
    private String sku;
}
