package com.gbsfo.ecommerce.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gbsfo.ecommerce.utils.identifiable.IdentifiableDto;
import com.gbsfo.ecommerce.utils.jackson.ApiJacksonSettings;
import lombok.AllArgsConstructor;
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
public class ItemDto extends IdentifiableDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("price")
    private BigDecimal price;
}

