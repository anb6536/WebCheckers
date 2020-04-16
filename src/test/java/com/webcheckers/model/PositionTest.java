package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PositionTest {
    @Test
    public void equalityTest() {
        int row = 12;
        int column = 123;
        Position position1 = Position.makePosition(row, column);
        Position position2 = Position.makePosition(row, column);
        assertEquals(position1, position2, "Positions 1 and 2 should be equal since they're the same row & column");
    }
    @Test
    void invertTest() {
        int row = 7;
        int col = 2;
        Position position1 = Position.makePosition(row, col);
        Position position2 = position1.invertPosition();
        assertEquals(position1.invertPosition(), position2);
    }
}