package com.webcheckers.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        Space space = boardView.getSpace(position);
        Assertions.assertEquals(space.getColor(), Space.Color.DARK);
        Assertions.assertEquals(space.getPiece().getColor(), Piece.Color.WHITE );
        Assertions.assertEquals(space.getPiece().getType(), Piece.Type.SINGLE );
    }
}
