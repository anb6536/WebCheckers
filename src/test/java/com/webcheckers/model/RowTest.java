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
        this.boardView = Board.makeBoard().getBoardView();
        this.rows = boardView.getRows();
    }

    @Test
    void getIndexTest(){
        for ( int i=0 ; i<7 ; i++ ){
            Assertions.assertEquals(rows.get(i).getIndex(), i);
        }
    }

    @Test
    void getSpacesTest(){
        for ( int i=0 ; i<7 ; i++ ){
            Row row = rows.get(i);
            for ( int j=0 ; j<7 ; j++ ){
                Assertions.assertEquals(row.getSpace(j), boardView.getSpace(i, j));
            }
        }
    }
}
