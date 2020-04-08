package com.webcheckers.model;

/**
 * a space with an index, a color, and the piece that may or may not be on it
 */
public class Space implements Comparable<Space> {
    /**
     * the color the space
     */
    public enum Color {
        DARK, LIGHT
    }

    private Color color;
    private int cellIdx;
    private Piece piece;

    /**
     * create a piece with cellIdx index
     * 
     * @param cellIdx the index of the space
     */
    public Space(int cellIdx) {
        this.cellIdx = cellIdx;
        this.piece = null;
    }

    public Space(int cellIdx, Color color) {
        this.cellIdx = cellIdx;
        this.color = color;
        this.piece = null;
    }

    public Space(int cellIdx, Color color, Piece piece) {
        this.cellIdx = cellIdx;
        this.color = color;
        this.piece = piece;
    }

    /**
     * get the index of the space
     * 
     * @return the space index
     */
    public int getCellIdx() {
        return this.cellIdx;
    }

    /**
     * set the piece on the space
     * 
     * @param piece the piece that should be put on the space (can be null)
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * set the piece on the space
     * 
     * @param piece The color of the piece that should be put on the space
     */
    public void setPiece(Piece.Color color) {
        this.piece = new Piece(color);
    }

    /**
     * set the color of the piece (this is set nearly immediately when it's created)
     * 
     * @param color the color to set the space to
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * simple getter
     * 
     * @return the color of the space
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * whether this is a possible location a piece could move to in this turn
     * 
     * @return whether the space is dark and no piece is on it
     */
    public boolean isValid() {
        if (this.piece == null && this.color == Color.DARK) {
            return true;
        }
        return false;
    }

    /**
     * simple getter
     * 
     * @return the piece on the space (can be null)
     */
    public Piece getPiece() {
        return this.piece;
    }

    /**
     * A comparator that exists just to have rows be correct when iterating
     * 
     * @param other the other space to compare to
     */
    @Override
    public int compareTo(Space other) {
        return Integer.compare(this.cellIdx, other.cellIdx);
    }
}
