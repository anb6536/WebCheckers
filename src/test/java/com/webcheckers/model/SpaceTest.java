package com.webcheckers.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SpaceTest {
    private Space darkSpace;
    private Space lightSpace;

    @BeforeEach
    public void setup() {
        darkSpace = new Space(0);
        darkSpace.setColor(Space.Color.DARK);
        lightSpace = new Space(1);
        lightSpace.setColor(Space.Color.LIGHT);
    }

    @Test
    public void testColor() {
        Assertions.assertEquals(darkSpace.getColor(), Space.Color.DARK);
        Assertions.assertEquals(lightSpace.getColor(), Space.Color.LIGHT);
    }

    @Test
    public void testCellId() {
        Assertions.assertEquals(darkSpace.getCellIdx(), 0);
        Assertions.assertEquals(lightSpace.getCellIdx(), 1);
    }

    @Test
    public void testPiece() {
        Assertions.assertNull(darkSpace.getPiece());
        Assertions.assertNull(lightSpace.getPiece());

        darkSpace.setPiece(Piece.Color.RED);
        lightSpace.setPiece(Piece.Color.WHITE);

        Assertions.assertEquals(darkSpace.getPiece().getColor(), Piece.Color.RED);
        Assertions.assertEquals(lightSpace.getPiece().getColor(), Piece.Color.WHITE);
    }

    @Test
    public void valid() {
        Assertions.assertTrue(darkSpace.isValid());

        darkSpace.setPiece(Piece.Color.WHITE);

        Assertions.assertFalse(darkSpace.isValid());
        Assertions.assertFalse(lightSpace.isValid());
    }
}
