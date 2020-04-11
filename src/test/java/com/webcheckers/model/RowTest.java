package com.webcheckers.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RowTest {
    private BoardView boardView;
    private List<Row> rows;

    @BeforeEach
    void initRow(){
        Board board = Board.makeBoard();
        board.addPieces();
        this.boardView = board.getBoardView();
        this.rows = boardView.getRows();
    }

    @Test
    void getIndexTest(){
        for ( int i=0 ; i<8 ; i++ ){
            Assertions.assertEquals(rows.get(i).getIndex(), i);
        }
    }

    @Test
    void getSpaceTest(){
        for ( int i=0 ; i<8 ; i++ ){
            Row row = rows.get(i);
            for ( int j=0 ; j<8 ; j++ ){
                Assertions.assertEquals(row.getSpace(j), boardView.getSpace(i, j));
            }
        }
    }

    @Test
    void getSpacesTest(){
        for ( int i=0 ; i<8 ; i++ ){
            List<Space> spaces = rows.get(i).getSpaces();
            for ( int j=0 ; j<8 ; j++ ){
                Assertions.assertEquals(spaces.get(j), boardView.getSpace(i, j));
            }
        }
    }

    @Test
    void hasRedPieceTest(){
        for ( int i=5; i<8; i++ ){
            Assertions.assertTrue(rows.get(i).hasRedPieces());
        }

        BoardView newBoardView = Board.makeBoard().getBoardView();
        List<Row> newRows = newBoardView.getRows();
        for ( int j=5; j<8; j++ ){
            Assertions.assertFalse(newRows.get(j).hasRedPieces());
        }
    }

    @Test
    void hasWhitePieceTest(){
        for ( int i=0; i<3; i++ ){
            Assertions.assertTrue(rows.get(i).hasWhitePieces());
        }

        BoardView newBoardView = Board.makeBoard().getBoardView();
        List<Row> newRows = newBoardView.getRows();
        for ( int j=0; j<3; j++ ){
            Assertions.assertFalse(newRows.get(j).hasWhitePieces());
        }
    }
}
