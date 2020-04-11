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
        for( int i=0 ; i<8 ; i++ ){
            for( int j=0 ; j<8 ; j++ ){
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

    @Test
    void validateMoveTest(){

        //Testing Red move
        int redRow = 5;
        int newRedRow = 4;
        int redCol = 0;
        int newRedCol = 1;
        Position redStart = Position.makePosition(redRow, redCol);
        Position redEnd = Position.makePosition(newRedRow, newRedCol);
        Move redMove = Move.createMove(redStart, redEnd);
        Assertions.assertTrue(board.validateMove(redMove, false).getKey());

        //Testing White move
        int whiteRow = 5;
        int newWhiteRow = 4;
        int whiteCol = 0;
        int newWhiteCol = 1;
        Position whiteStart = Position.makePosition(whiteRow, whiteCol);
        Position whiteEnd = Position.makePosition(newWhiteRow, newWhiteCol);
        Move whiteMove = Move.createMove(whiteStart, whiteEnd);
        Assertions.assertTrue(board.validateMove(whiteMove, true).getKey());
    }

    @Test
    void makeMoveTest(){

        //Testing Red move
        int redRow = 5;
        int newRedRow = 4;
        int redCol = 0;
        int newRedCol = 1;
        Position redStart = Position.makePosition(redRow, redCol);
        Position redEnd = Position.makePosition(newRedRow, newRedCol);
        Move redMove = Move.createMove(redStart, redEnd);
        Assertions.assertTrue(board.makeMove(redMove, false));

        //Testing White move
        int whiteRow = 5;
        int newWhiteRow = 4;
        int whiteCol = 0;
        int newWhiteCol = 1;
        Position whiteStart = Position.makePosition(whiteRow, whiteCol);
        Position whiteEnd = Position.makePosition(newWhiteRow, newWhiteCol);
        Move whiteMove = Move.createMove(whiteStart, whiteEnd);
        Assertions.assertTrue(board.makeMove(whiteMove, true));
    }

}
