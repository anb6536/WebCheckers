package com.webcheckers.model;

/**
 * Stores information about a move
 */
public class MoveInformation {
    Move move;
    Piece removed;
    Position removedPosition;

    public MoveInformation(Move move, Piece removed, Position removedPosition) {
        this.move = move;
        // removed and removedPosition can be null for the sake of differentiating
        // between simple move and jump move
        this.removed = removed;
        this.removedPosition = removedPosition;
    }

    public MoveInformation(Move move) {
        this.move = move;
        // Make removed position and null if not provided for clarity
        // Represents single move if these don't exist
        this.removedPosition = null;
        this.removed = null;
    }

    /**
     * Gets the move associated with this class
     * 
     * @return A move
     */
    public Move getMove() {
        return move;
    }

    /**
     * Gets the actual piece that was removed if one was removed
     * 
     * @return a Piece that was removed by this move
     */
    public Piece getRemovedPiece() {
        return removed;
    }

    /**
     * Gets the position of piece that may have been removed by this move
     * 
     * @return gets the removed piece or null
     */
    public Position getRemovedPosition() {
        return removedPosition;
    }

    /**
     * Evaluates whether or not a move was a jump move
     * 
     * @return true if a move jumped over another piece
     */
    public boolean isJumpMove() {
        return (removedPosition != null && removed != null);
    }

    public boolean isRedHomeRow() {
        return move.end.row == 7;
    }

    public boolean isWhiteHomeRow() {
        return move.end.row == 0;
    }

    public boolean isKingable(boolean whiteMove) {
        if (whiteMove) {
            return isRedHomeRow();
        } else {
            return isWhiteHomeRow();
        }
    }
}