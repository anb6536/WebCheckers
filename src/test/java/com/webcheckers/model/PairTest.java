package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.webcheckers.util.Pair;
import org.junit.jupiter.api.Test;

import javax.print.DocFlavor;

class PairTest {
    // Creating test pairs of different types
    private static final String TEST_STRING = "Orange Juice";
    private static final Integer TEST_INT = 26;
    private static final Boolean TEST_BOOL = true;
    private static final Float TEST_FLOAT = 3.14f;

    private static final String TEST_STRING2 = "Torsion";
    private static final int TEST_INT2 = 45;

    // Pair to test
    private static Pair<Integer, String> testPair1 = new Pair<>(TEST_INT, TEST_STRING);
    // Reversed order pair
    private static Pair<String, Integer> testPair2 = new Pair<>(TEST_STRING, TEST_INT);
    // Pair of other types
    private static Pair<Boolean, Float> testPair3 = new Pair<Boolean, Float>(TEST_BOOL, TEST_FLOAT);

    @Test
    void createPairTest() {
        Pair<String, Integer> testPair = new Pair<>(TEST_STRING2, TEST_INT2);
        assertEquals(testPair.getKey(), TEST_STRING2);
        assertEquals(testPair.getValue(), TEST_INT2);
    }



    @Test
    void getKeyTest() {
        assertEquals(testPair1.getKey(), TEST_INT);
        assertEquals(testPair2.getKey(), TEST_STRING);
        assertEquals(testPair3.getKey(), TEST_BOOL);
    }

    @Test
    void getValueTest() {
        assertEquals(testPair1.getValue(), TEST_STRING);
        assertEquals(testPair2.getValue(), TEST_INT);
        assertEquals(testPair3.getValue(), TEST_FLOAT);
    }
}
