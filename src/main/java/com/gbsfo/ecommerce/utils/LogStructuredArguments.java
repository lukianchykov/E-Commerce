package com.gbsfo.ecommerce.utils;

import net.logstash.logback.argument.StructuredArgument;
import net.logstash.logback.argument.StructuredArguments;

public class LogStructuredArguments {

    private static final String ORDER_ID = "orderId";

    private static final String ITEM_ID = "itemId";

    private static final String PAYMENT_ID = "paymentId";

    private LogStructuredArguments() {
    }

    public static StructuredArgument orderId(long orderId) {
        return StructuredArguments.keyValue(ORDER_ID, orderId);
    }

    public static StructuredArgument itemId(long itemId) {
        return StructuredArguments.keyValue(ITEM_ID, itemId);
    }

    public static StructuredArgument paymentId(long paymentId) {
        return StructuredArguments.keyValue(PAYMENT_ID, paymentId);
    }
}