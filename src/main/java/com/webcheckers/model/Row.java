package com.webcheckers.model;

import java.util.Iterator;
import java.util.List;

/**
 * The row on a board with info about the spaces and transitively pieces on it
 */
public class Row implements Comparable<Row> {
    private int index;
    private List<Space> spaces;

    /**
     * create a row with an index of which row it is and a premade list of spaces
     * and pieces on it
     * 
     * @param index  the index of the ro w
     * @param spaces the premade spaces
     */
    public Row(int index, List<Space> spaces) {
        this.index = index;
        this.spaces = spaces;
    }

    /**
     * simple getter
     * 
     * @return the index of the row
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * simple getter
     * 
     * @return the list of spaces in the row
     */
    public List<Space> getSpaces() {
        return this.spaces;
    }

    /**
     * get an iterator to iterate through the spaces on the board
     * 
     * @return the iterator
     */
    public Iterator<Space> iterator() {
        return this.spaces.iterator();
    }

    public Space getSpace(int column) {
        if (column >= spaces.size()) {
            throw new ArrayIndexOutOfBoundsException("Column out of bounds");
        }
        return spaces.get(column);
    }

    // This is just for sorting by rows so that processing things make actual sense
    @Override
    public int compareTo(Row other) {
        return Integer.compare(this.index, other.index);
    }
}
