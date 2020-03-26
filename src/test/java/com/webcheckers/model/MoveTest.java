package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MoveTest {
    @Test
    public void reverseTest() {
        Position position1 = Position.makePosition(1, 0);
        Position position2 = Position.makePosition(1, 3);
        Move move = Move.createMove(position1, position2);
        Move move2 = Move.createMove(position1, position2);
        move.reverse();
        assertEquals(move, move2, "Move reversed should equal move2");
    }

    @Test
    public void equalityTest() {
        Position position1 = Position.makePosition(1, 0);
        Position position2 = Position.makePosition(1, 3);
        Move move = Move.createMove(position1, position2);
        Move move2 = Move.createMove(position1, position2);
        assertEquals(move, move2, "Both moves should be equal since the positions are the same");
        assertEquals(move.hashCode(), move2.hashCode(), "Both hashcodes should be the same since they are equal");
    }
}