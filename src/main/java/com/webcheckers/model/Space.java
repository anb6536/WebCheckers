package com.webcheckers.model;

public class Space {

    public enum Color{
        DARK,
        LIGHT;
    }

    private Color color;
    private int cellIdx;
    private Piece piece;

    public Space( int cellIdx ){
        this.cellIdx = cellIdx;
        this.piece = null;
    }

    public int getCellIdx(){
        return this.cellIdx;
    }

    public void setPiece(Piece piece){
        this.piece = piece;
    }

    public void setColor( Color color ){
        this.color = color;
    }

    public Color getColor(){
        return this.color;
    }

    public boolean isValid(){
        if ( this.piece == null && this.color==Color.DARK ){
            return true;
        }
        return false;
    }

    public Piece getPiece(){
        return this.piece;
    }
}
