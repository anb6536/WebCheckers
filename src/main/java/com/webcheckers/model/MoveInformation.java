package com.webcheckers.model;

public class MoveInformation {
    Move move;
    Piece removed;
    Position removedPosition;

    public MoveInformation(Move move, Piece removed, Position removedPosition) {
        this.move = move;
        // removed and removedPosition can be null for the sake of differentiating between simple move and jump move
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

    public Move getMove() {
        return move;
    }

    public Piece getRemovedPiece() {
        return removed;
    }

    public Position getRemovedPosition() {
        return removedPosition;
    }

    public boolean isJumpMove() {
        return (removedPosition != null && removed != null);
    }
}