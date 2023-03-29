package com.gbsfo.ecommerce.utils;

public final class Constants {

    public static final String API_VERSION_PREFIX_V1 = "/api/v1";

    public static final String API_OFFSET_REQUEST_PARAMETER = "offset";

    public static final String API_LIMIT_REQUEST_PARAMETER = "limit";

    public static final int API_LIMIT_REQUEST_PARAMETER_MAX_VALUE = 100;

    public static final String API_ERROR_INTERNAL_ERROR_MESSAGE = "Internal error";

    public static final String API_VALIDATION_ERROR_MESSAGE_PAGINATION_OFFSET = "Must be positive value";

    public static final String API_VALIDATION_ERROR_MESSAGE_PAGINATION_LIMIT = "Must be more than 0 and less than or equal to " + API_LIMIT_REQUEST_PARAMETER_MAX_VALUE;

    public static final String API_VALIDATION_ERROR_MESSAGE_BLANK_STRING = "Must not be blank";

    private Constants() {

    }
}
