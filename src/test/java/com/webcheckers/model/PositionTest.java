package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PositionTest {
    @Test
    public void equalityTest() {
        int row = 12;
        int column = 123;
        Position position1 = new Position(row, column);
        Position position2 = new Position(row, column);
        assertEquals(position1, position2, "Positions 1 and 2 should be equal since they're the same row & column");
    }
}