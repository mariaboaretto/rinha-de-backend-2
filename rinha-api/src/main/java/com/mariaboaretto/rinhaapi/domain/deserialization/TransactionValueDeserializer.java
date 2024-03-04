package com.mariaboaretto.rinhaapi.domain.deserialization;

import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

// Makes sure transaction value is deserialized to Integer
public class TransactionValueDeserializer extends StdDeserializer {
    public TransactionValueDeserializer() {
        this(null);
    }

    public TransactionValueDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        if (node.isInt()) {
            return node.intValue();
        } else if (node.isFloat() || node.isDouble()) {
            throw new IllegalArgumentException("Transaction value must be a whole number.");
        }
        throw new IllegalArgumentException("Transaction value must be a whole number.");
    }
}
