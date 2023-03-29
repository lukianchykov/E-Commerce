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
@ApiJacksonSettings
@AllArgsConstructor
public class ItemLookupPublicApiRequest {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("offset")
    private int offset;

    @JsonProperty("limit")
    private int limit;

    public ItemLookupPublicApiRequest(String name, BigDecimal price, int offset, int limit) {
        this.name = name;
        this.price = price;
        this.offset = offset;
        this.limit = limit;
    }
}
