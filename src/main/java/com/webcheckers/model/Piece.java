package com.webcheckers.model;

/**
 * a piece with color and whether it is a king
 */
public class Piece {
    /**
     * the color of the piece
     */
    public enum color{
        RED,
        WHITE
    }

    /**
     * The type of the piece
     */
    public enum type{
        SINGLE,
        KING
    }

    private type type;
    private color color;

    /**
     * create a piece
     * @param color the color the piece
     */
    public Piece( color color ){
        this.color = color;
        this.type = type.SINGLE;
    }

    /**
     * get the type of piece this is (king or single)
     * @return the type
     */
    public type getType(){
        return this.type;
    }

    /**
     * get the color of this piece
     * @return the color
     */
    public color getColor(){
        return this.color;
    }

}
