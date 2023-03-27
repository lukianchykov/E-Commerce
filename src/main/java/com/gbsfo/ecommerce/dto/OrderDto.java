package com.gbsfo.ecommerce.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gbsfo.ecommerce.utils.identifiable.IdentifiableDto;
import com.gbsfo.ecommerce.utils.jackson.ApiJacksonSettings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiJacksonSettings
@EqualsAndHashCode(callSuper = true)
public class OrderDto extends IdentifiableDto {

    @JsonProperty("order_number")
    private String number;

    @JsonProperty("order_status")
    private OrderStatus orderStatus;

    @JsonProperty("items")
    @Builder.Default
    private List<ItemDto> items = new ArrayList<>();

    @JsonProperty("payments")
    @Builder.Default
    private List<PaymentDto> payments = new ArrayList<>();

    public enum OrderStatus {
        @JsonProperty("CREATED") CREATED,
        @JsonProperty("PROCESSING") PROCESSING,
        @JsonProperty("SHIPPING") SHIPPING,
        @JsonProperty("DELIVERED") DELIVERED
    }
}
