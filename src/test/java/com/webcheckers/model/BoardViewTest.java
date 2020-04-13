package com.webcheckers.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.List;

public class BoardViewTest {
    private BoardView boardView;

    @BeforeEach
    void initBoardView(){
        Board board = Board.makeBoard();
        board.addPieces();
        this.boardView = board.getBoardView();
    }

    @Test
    void getSpaceTest(){
        int row = 0;
        int col = 1;

        Position position = Position.makePosition(row, col);
        Space space1 = boardView.getSpace(position);
        Assertions.assertEquals(space1.getColor(), Space.Color.DARK);
        Assertions.assertEquals(space1.getPiece().getColor(), Piece.Color.WHITE );
        Assertions.assertEquals(space1.getPiece().getType(), Piece.Type.SINGLE );

        Space space2 = boardView.getSpace(row, col);
        Assertions.assertEquals(space2.getColor(), Space.Color.DARK);
        Assertions.assertEquals(space2.getPiece().getColor(), Piece.Color.WHITE );
        Assertions.assertEquals(space2.getPiece().getType(), Piece.Type.SINGLE );

        try{
            Space space = boardView.getSpace(10, 0);
        } catch (ArrayIndexOutOfBoundsException e) {
            Assertions.assertTrue(e.getMessage().equals("Row index out of bounds"));
        }
    }

    @Test
    void getRowTest(){
        Assertions.assertNotNull(boardView.getRows());
    }

    @Test
    void cloneTest(){
        BoardView copyBoard = boardView.clone();
        Assertions.assertEquals(boardView.getRows(), copyBoard.getRows());
    }

    @Test
    void hasRedPieceTest(){
        Assertions.assertTrue(boardView.hasRedPieces());

        BoardView empty = Board.makeBoard().getBoardView();
        Assertions.assertFalse(empty.hasRedPieces());
    }

    @Test
    void hasWhitePieceTest(){
        Assertions.assertTrue(boardView.hasWhitePieces());

        BoardView empty = Board.makeBoard().getBoardView();
        Assertions.assertFalse(empty.hasWhitePieces());
    }
}
