package com.webcheckers.model;

public class MoveInformation {
    Move move;
    Piece removed;
    Position removedPosition;

    public MoveInformation(Move move, Piece removed, Position removedPosition) {
        this.move = move;
        this.removed = removed;
        this.removedPosition = removedPosition;
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
}