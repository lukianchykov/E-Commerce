package com.gbsfo.ecommerce.controller.exception;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gbsfo.ecommerce.utils.jackson.ApiJacksonSettings;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ApiJacksonSettings
public class PublicApiErrorResponse {

    @JsonProperty("request_id")
    private final String requestId;

    @JsonProperty("errors")
    private final List<PublicApiError> errors;

    @Getter
    @RequiredArgsConstructor
    @ApiJacksonSettings
    public static class PublicApiError {

        @JsonProperty("message")
        private final String message;

    }

    @Getter
    @ApiJacksonSettings
    public static class PublicApiValidationError extends PublicApiError {

        @JsonProperty("property")
        private final String property;

        public PublicApiValidationError(String message, String property) {
            super(message);
            this.property = property;
        }
    }

}
