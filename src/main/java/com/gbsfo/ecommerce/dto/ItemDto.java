package com.gbsfo.ecommerce.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gbsfo.ecommerce.utils.jackson.ApiJacksonSettings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiJacksonSettings
public class ItemDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("price")
    private BigDecimal price;
}

