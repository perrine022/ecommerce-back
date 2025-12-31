package com.tradeall.tradefood.dto.sellsy;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Désérialiseur Integer sécurisé pour gérer les valeurs étranges renvoyées par Sellsy (ex: "W10=").
 */
public class SafeIntegerDeserializer extends JsonDeserializer<Integer> {

    private static final Logger log = LoggerFactory.getLogger(SafeIntegerDeserializer.class);

    @Override
    public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        if (value == null || value.isEmpty() || "W10=".equals(value)) {
            return null;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.warn("Valeur non numérique reçue pour un champ Integer: {}", value);
            return null;
        }
    }
}
