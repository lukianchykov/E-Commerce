package com.gbsfo.ecommerce.domain;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class BigDecimalAvroDeserializer extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // Handle Avro encoding where price is inside an object
        if (node.has("bytes")) {
            String priceString = node.get("bytes").asText();
            return new BigDecimal(priceString);
        }

        // Default case: price is a plain number
        return new BigDecimal(node.asText());
    }
}

