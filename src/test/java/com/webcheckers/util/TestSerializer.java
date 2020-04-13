package com.webcheckers.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class TestSerializer {
    public static final String testString = "help=sometext&variable=unknown";

    @Test
    void testSerializer() {
        Map<String, String> values = new HashMap<String, String>();
        values.put("help", "sometext");
        values.put("variable", "unknown");
        assertTrue(Serializer.serialize(values).equals(testString));
    }
}