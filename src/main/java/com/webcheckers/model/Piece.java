package com.webcheckers.model;

/**
 * a piece with color and whether it is a king
 */
public class Piece {
    /**
     * the color of the piece
     */
    public enum Color {
        RED, WHITE
    }

    /**
     * The type of the piece
     */
    public enum Type {
        SINGLE, KING
    }

    private Type type;
    private Color color;

    /**
     * create a piece
     * 
     * @param color the color the piece
     */
    public Piece(Color color) {
        this.color = color;
        this.type = Type.SINGLE;
    }

    /**
     * get the type of piece this is (king or single)
     * 
     * @return the type
     */
    public Type getType() {
        return this.type;
    }

    /**
     * get the color of this piece
     * 
     * @return the color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * set the type of this piece to king
     */
    public void KingMe() {
        this.type = Type.KING;
    }
}
