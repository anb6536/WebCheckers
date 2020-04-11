package com.webcheckers.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RowTest {
    private List<Row> rows;

    @BeforeEach
    void initRow(){
        this.rows = Board.makeBoard().getBoardView().getRows();
    }

    @Test
    void getIndexTest(){
        for ( int i=0 ; i<7 ; i++ ){
            Assertions.assertEquals(rows.get(i).getIndex(), i);
        }
    }
}
