package com.webcheckers.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BoardTest {
    private Board board;
    private Board flipped;

    @BeforeEach
    void initBoard(){
        this.board = Board.makeBoard();
        this.board.addPieces();
        this.flipped = board.flipBoard();
    }

    @Test
    void hasRedPieceTest(){
        Assertions.assertTrue(board.hasRedPieces());
    }

    @Test
    void hasWhitePieceTest(){
        Assertions.assertTrue(board.hasWhitePieces());
    }

    @Test
    void flipBoardTest(){
        for( int i=0 ; i<7 ; i++ ){
            for( int j=0 ; j<7 ; j++ ){
                Space space1 = board.getBoardView().getSpace(i, j);
                Space space2 = flipped.getBoardView().getSpace(7-i, 7-j);
                if ( space1.getPiece() != null && space2.getPiece() != null ) {
                    Assertions.assertEquals(space1.getPiece().getType(), space2.getPiece().getType());
                    Assertions.assertEquals(space1.getPiece().getColor(), space2.getPiece().getColor());
                }
                Assertions.assertEquals(space1.getColor(), space2.getColor());
            }
        }
    }

}
