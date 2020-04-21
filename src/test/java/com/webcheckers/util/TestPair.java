package com.webcheckers.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TestPair {
    @Test
    void testWorks() {
        String value = "Hello";
        String key = "Man";
        Pair<String, String> pair = new Pair<String, String>(key, value);
        assertTrue(pair.getKey().equals(key));
        assertTrue(pair.getValue().equals(value));
        Pair<String, String> pair2 = Pair.createPair(key, value);
        assertTrue(pair2.getKey().equals(key));
        assertTrue(pair2.getValue().equals(value));
    }
}