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
public class PaymentLookupPublicApiRequest {

    @JsonProperty("id")
    private String id;

    @JsonProperty("number")
    private String number;

    @JsonProperty("sum")
    private BigDecimal sum;

    @JsonProperty("offset")
    private int offset;

    @JsonProperty("limit")
    private int limit;

    public PaymentLookupPublicApiRequest(String number, BigDecimal sum, int offset, int limit) {
        this.number = number;
        this.sum = sum;
        this.offset = offset;
        this.limit = limit;
    }
}
