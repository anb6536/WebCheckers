package com.webcheckers.model;

public class Piece {
    public enum color{
        RED,
        WHITE
    }

    public enum type{
        SINGLE,
        KING
    }

    private type type;
    private color color;

    public Piece( color color ){
        this.color = color;
        this.type = type.SINGLE;
    }

    public color getRed(){
        return color.RED;
    }

    public color getWhite(){
        return color.WHITE;
    }
    public type getType(){
        return this.type;
    }

    public color getColor(){
        return this.color;
    }

}
