package com.webcheckers.model;

import java.util.Iterator;
import java.util.List;

public class Row {
    private int index;
    private List<Space> spaces;

    public Row( int index, List<Space> spaces ){
        this.index = index;
        this.spaces = spaces;
    }

    public int getIndex(){
        return this.index;
    }

    public List<Space> getSpaces(){
        return this.spaces;
    }

    public Iterator<Space> iterator(){
        return this.spaces.iterator();
    }
}
