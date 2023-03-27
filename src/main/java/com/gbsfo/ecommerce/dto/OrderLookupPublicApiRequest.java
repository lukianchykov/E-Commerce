package com.gbsfo.ecommerce.dto;

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
public class OrderLookupPublicApiRequest {

    @JsonProperty("id")
    private String id;

    @JsonProperty("number")
    private String number;

    @JsonProperty("order_status")
    private String orderStatus;

    @JsonProperty("offset")
    private int offset;

    @JsonProperty("limit")
    private int limit;

    public OrderLookupPublicApiRequest(String id) {
        this.id = id;
    }

    public OrderLookupPublicApiRequest(String number, String orderStatus, int offset, int limit) {
        this.number = number;
        this.orderStatus = orderStatus;
        this.offset = offset;
        this.limit = limit;
    }
}