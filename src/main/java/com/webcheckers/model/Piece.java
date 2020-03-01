package com.webcheckers.model;

/**
 * a piece with color and whether it is a king
 */
public class Piece {
    /**
     * the color of the piece
     * // TODO ENUMS ARE CAPITAL
     */
    public enum color{
        RED,
        WHITE
    }

    /**
     *  TODO ENUMS ARE CAPITAL
     * whether the piece is kinged
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
        // TODO this gets the variable type and gets single in a non static way
        this.type = type.SINGLE;
    }

    // TODO i don't think this is the proper way to access this? (at any rate this isn't used)
    public color getRed(){
        return color.RED;
    }

    public color getWhite(){
        return color.WHITE;
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
