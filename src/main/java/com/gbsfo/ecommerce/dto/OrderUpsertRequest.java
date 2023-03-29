package com.gbsfo.ecommerce.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gbsfo.ecommerce.utils.jackson.ApiJacksonSettings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.gbsfo.ecommerce.utils.Constants.API_VALIDATION_ERROR_MESSAGE_BLANK_STRING;

@Data
@Builder
@NoArgsConstructor
@ApiJacksonSettings
@AllArgsConstructor
public class OrderUpsertRequest {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("order_number")
    @NotBlank(message = API_VALIDATION_ERROR_MESSAGE_BLANK_STRING)
    private String number;

    @JsonProperty("order_status")
    @NotBlank(message = API_VALIDATION_ERROR_MESSAGE_BLANK_STRING)
    private OrderStatus orderStatus;

    @JsonProperty("items")
    private List<ItemDto> items;

    @JsonProperty("payments")
    private List<PaymentDto> payments;

    public enum OrderStatus {
        @JsonProperty("CREATED") CREATED,
        @JsonProperty("PROCESSING") PROCESSING,
        @JsonProperty("SHIPPING") SHIPPING,
        @JsonProperty("DELIVERED") DELIVERED
    }

}