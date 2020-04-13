package com.webcheckers.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class TestDeserializer {
    public static final String testString = "help=sometext&variable=unknown";

    @Test
    void testDeserializes() {
        Map<String, String> values = Deserialzer.deserialize(testString);
        assertTrue(values.get("help").equals("sometext"));
        assertTrue(values.get("variable").equals("unknown"));
    }

}