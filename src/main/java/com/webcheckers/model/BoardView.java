package com.webcheckers.model;

import java.util.Iterator;
import java.util.List;

/**
 * a board with all the information about board and pieces
 */
public class BoardView {
    private List<Row> rows;

    // create an already created board
    public BoardView(List<Row> rows) {
        this.rows = rows;
    }

    /**
     * simple getter
     * 
     * @return the reference to the rows
     */
    public List<Row> getRows() {
        return this.rows;
    }

    /**
     * iterator so that we can iterate through the rows of the board
     * 
     * @return an iterator through the rows
     */
    public Iterator<Row> iterator() {
        return this.rows.iterator();
    }
}
