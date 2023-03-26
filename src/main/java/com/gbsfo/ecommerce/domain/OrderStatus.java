package com.gbsfo.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderStatus {
    @JsonProperty("created") CREATED,
    @JsonProperty("processing") PROCESSING,
    @JsonProperty("shipping") SHIPPING,
    @JsonProperty("delivered") DELIVERED
}
