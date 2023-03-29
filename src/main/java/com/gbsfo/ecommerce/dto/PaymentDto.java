package com.gbsfo.ecommerce.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class PaymentDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("number")
    private String number;

    @JsonProperty("sum")
    private BigDecimal sum;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX", timezone = "UTC")
    @JsonProperty("payment_date_time")
    private Instant paymentDateTime;
}


