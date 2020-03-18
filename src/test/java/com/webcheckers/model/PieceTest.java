package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.webcheckers.model.Piece.Type.SINGLE;
import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-Tier")
public class PieceTest {
    private Piece redPiece;
    private Piece whitePiece;

    @BeforeEach
    void setup(){
        redPiece = new Piece(Piece.Color.RED);
        whitePiece = new Piece(Piece.Color.WHITE);
    }

    @Test
    void getType(){
        assertNotNull(redPiece.getType(), "Piece Type is accessible");
        assertNotNull(whitePiece.getType(), "Piece Type is accessible");

        assertEquals(redPiece.getType(), SINGLE, "Red Piece is of Single type");
        assertEquals(whitePiece.getType(), SINGLE, "White Piece is of Single type");
    }

    @Test
    void getColor(){
        assertEquals(redPiece.getColor(), Piece.Color.RED, "Color of the Red piece is Red");
        assertEquals(whitePiece.getColor(), Piece.Color.WHITE, "Color of the White piece is White");

        assertNotEquals(redPiece.getColor(), Piece.Color.WHITE, "Color of the Red piece is not White");
        assertNotEquals(whitePiece.getColor(), Piece.Color.RED, "Color of the White piece is not Red");
    }
}
