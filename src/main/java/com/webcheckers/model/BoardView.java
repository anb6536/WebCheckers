package com.webcheckers.model;

import java.util.Iterator;
import java.util.List;

public class BoardView {
    private List<Row> rows;

    public BoardView( List<Row> rows ){
        this.rows = rows;
    }

    public Iterator<Row> iterator(){
        return this.rows.iterator();
    }
}
