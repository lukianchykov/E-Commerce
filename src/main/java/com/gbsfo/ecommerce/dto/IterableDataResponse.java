package com.gbsfo.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gbsfo.ecommerce.utils.jackson.ApiJacksonSettings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiJacksonSettings
public class IterableDataResponse<T> extends DataResponse<T> {

    @JsonProperty("has_next")
    private boolean hasNext;

    public IterableDataResponse(T data, boolean hasNext) {
        super(data);
        this.hasNext = hasNext;
    }
}
