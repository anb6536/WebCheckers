package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MoveTest {
    @Test
    public void reverseTest() {
        Position position1 = new Position(1, 0);
        Position position2 = new Position(1, 3);
        Move move = new Move(position1, position2);
        Move move2 = new Move(position2, position1);
        move.reverse();
        assertEquals(move, move2, "Move reversed should equal move2");
    }

    @Test
    public void equalityTest() {
        Position position1 = new Position(1, 0);
        Position position2 = new Position(1, 3);
        Move move = new Move(position1, position2);
        Move move2 = new Move(position1, position2);
        assertEquals(move, move2, "Both moves should be equal since the positions are the same");
        assertEquals(move.hashCode(), move2.hashCode(), "Both hashcodes should be the same since they are equal");
    }
}