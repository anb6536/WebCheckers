package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

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

    }

    @Test
    void getColor(){
        
    }
}
